package cz.cvut.fit.steuejan.wanderscope.trip.add_edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.DatePickerInfo.Companion.today
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.extension.toDateTime
import cz.cvut.fit.steuejan.wanderscope.app.livedata.LoadingMutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.livedata.mediator.PairMediatorLiveData
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent.Back
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.app.util.getDateFromDatePicker
import cz.cvut.fit.steuejan.wanderscope.trip.api.request.TripRequest
import cz.cvut.fit.steuejan.wanderscope.trip.repository.TripRepository
import kotlinx.coroutines.launch

class AddEditTripFragmentVM(
    title: Int,
    private val purpose: Purpose,
    private val tripRepository: TripRepository
) : BaseViewModel() {

    val title = MutableLiveData<Int>()

    init {
        this.title.value = title
    }

    val name = MutableLiveData<String>()
    val startDate = MutableLiveData<String?>(null)
    val endDate = MutableLiveData<String?>(null)
    val description = MutableLiveData<String>()

    val submitLoading = LoadingMutableLiveData()

    val validateName = name.switchMapSuspend {
        validator.validateIfNotEmpty(it)
    }

    val validateDates = PairMediatorLiveData(startDate, endDate).switchMapSuspend { (start, end) ->
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

    private var startDateInMillis: Long? = null
    private var endDateInMillis: Long? = null

    fun startDatePicker() {
        showDatePicker(DatePickerInfo(
            initialDate = startDateInMillis ?: endDateInMillis ?: today
        ) { dateInMillis ->
            viewModelScope.launch {
                startDateInMillis = dateInMillis
                startDate.value = getDateFromDatePicker(dateInMillis)
            }
        })
    }

    fun endDatePicker() {
        showDatePicker(DatePickerInfo(
            initialDate = endDateInMillis ?: startDateInMillis ?: today
        ) { dateInMillis ->
            viewModelScope.launch {
                endDateInMillis = dateInMillis
                endDate.value = getDateFromDatePicker(dateInMillis)
            }
        })
    }

    fun submit() {
        viewModelScope.launchIO {
            val tripRequest = TripRequest(
                name.value ?: return@launchIO,
                Duration(startDateInMillis?.toDateTime(), endDateInMillis?.toDateTime()),
                description.value,
                null
            )
            val result = when (purpose) {
                Purpose.CREATE -> tripRepository.createTrip(tripRequest)
                Purpose.EDIT -> TODO()
            }
            result.safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> handleFailure(it.error)
                    is Result.Loading -> submitLoading.value = true
                    is Result.Success -> navigateTo(Back)
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