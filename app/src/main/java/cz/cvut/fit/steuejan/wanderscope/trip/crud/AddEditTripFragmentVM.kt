package cz.cvut.fit.steuejan.wanderscope.trip.crud

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.DatePickerInfo.Companion.today
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.*
import cz.cvut.fit.steuejan.wanderscope.app.livedata.AnySingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.livedata.LoadingMutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.livedata.mediator.PairMediatorLiveData
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.app.util.getDateFromMillis
import cz.cvut.fit.steuejan.wanderscope.trip.api.request.TripRequest
import cz.cvut.fit.steuejan.wanderscope.trip.crud.bundle.EditTripBundle
import cz.cvut.fit.steuejan.wanderscope.trip.repository.TripRepository
import kotlinx.coroutines.launch

class AddEditTripFragmentVM(
    private val tripRepository: TripRepository
) : BaseViewModel() {

    val title = MutableLiveData<Int>()

    private var purpose: Purpose? = null
    private var tripId: Int? = null

    val name = MutableLiveData<String>()
    val startDate = MutableLiveData<String?>(null)
    val endDate = MutableLiveData<String?>(null)
    val description = MutableLiveData<String?>()

    val requestIsSuccess = AnySingleLiveEvent()

    val submitLoading = LoadingMutableLiveData()

    val validateName = name.switchMapSuspend {
        validator.validateIfNotEmpty(it)
    }

    private var shouldValidateDates = true
    private var startDateInMillis: Long? = null
    private var endDateInMillis: Long? = null

    val validateDates = PairMediatorLiveData(startDate, endDate).switchMapSuspend { (start, end) ->
        if (!shouldValidateDates) {
            return@switchMapSuspend OK
        }
        if (start.isNullOrBlank()) {
            startDateInMillis = null
        }
        if (end.isNullOrBlank()) {
            endDateInMillis = null
        }
        validator.validateDates(startDateInMillis, endDateInMillis)
    }

    val enableSubmit = ValidationMediator(
        validateName,
        validateDates
    )

    init {
        title.value = R.string.add_trip
        purpose = Purpose.CREATE
    }

    fun setupEditTrip(trip: EditTripBundle) {
        this.title.value = R.string.edit_trip
        purpose = Purpose.EDIT
        tripId = trip.id
        populateFields(trip)
    }

    private fun populateFields(trip: EditTripBundle) {
        viewModelScope.launch {
            shouldValidateDates = false
            name.value = trip.name
            startDate.value = trip.startDate?.millis?.let {
                startDateInMillis = it
                getDateFromMillis(it)
            }
            endDate.value = trip.endDate?.millis?.let {
                endDateInMillis = it
                getDateFromMillis(it)
            }
            description.value = trip.description
            shouldValidateDates = true
        }
    }

    fun startDatePicker() {
        showDatePicker(DatePickerInfo(
            initialDate = startDateInMillis ?: endDateInMillis ?: today
        ) { dateInMillis ->
            viewModelScope.launch {
                startDateInMillis = dateInMillis
                startDate.value = getDateFromMillis(dateInMillis)
            }
        })
    }

    fun endDatePicker() {
        showDatePicker(DatePickerInfo(
            initialDate = endDateInMillis ?: startDateInMillis ?: today
        ) { dateInMillis ->
            viewModelScope.launch {
                endDateInMillis = dateInMillis
                endDate.value = getDateFromMillis(dateInMillis)
            }
        })
    }

    fun submit() {
        viewModelScope.launchIO {
            val tripRequest = TripRequest(
                name = name.value ?: return@launchIO,
                duration = Duration(startDateInMillis?.toDateTime(), endDateInMillis?.toDateTime()),
                description = description.value.getOrNullIfBlank(),
                imageUrl = null
            )

            val result = when (purpose) {
                Purpose.CREATE -> tripRepository.createTrip(tripRequest)
                Purpose.EDIT -> tripRepository.editTrip(tripId ?: return@launchIO, tripRequest)
                null -> return@launchIO
            }

            result.safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> handleFailure(it.error)
                    is Result.Loading -> submitLoading.value = true
                    is Result.Success -> requestIsSuccess.publish()
                }
            }
        }
    }

    private fun handleFailure(error: Error) {
        submitLoading.value = false
        unexpectedError(error)
    }

    enum class Purpose {
        CREATE, EDIT
    }
}