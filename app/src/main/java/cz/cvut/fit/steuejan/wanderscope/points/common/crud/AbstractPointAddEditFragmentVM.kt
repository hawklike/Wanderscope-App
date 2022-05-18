package cz.cvut.fit.steuejan.wanderscope.points.common.crud

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.DatePickerInfo.Companion.today
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.ValidateDates
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.ValidateDates.NORMAL
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.extension.toNiceString
import cz.cvut.fit.steuejan.wanderscope.app.livedata.AnySingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.livedata.LoadingMutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.livedata.mediator.PairMediatorLiveData
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent.Back
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.points.common.api.request.PointRequest
import cz.cvut.fit.steuejan.wanderscope.points.common.repository.PointRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.joda.time.DateTime

abstract class AbstractPointAddEditFragmentVM<in Request : PointRequest>(
    protected val pointRepository: PointRepository<Request, *>,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(savedStateHandle) {

    val title = MutableLiveData<Int>()

    protected var purpose: Purpose? = null
    protected var tripId: Int? = null
    protected var pointId: Int? = null

    fun init(tripId: Int, @StringRes title: Int) {
        purpose = Purpose.CREATE
        this.title.value = title
        this.tripId = tripId
    }

    val findAccommodationEvent = SingleLiveEvent<String?>()
    val hideKeyboardEvent = AnySingleLiveEvent()

    protected var placeName: String? = null
    protected var placeId: String? = null
    protected var selectedTypePosition: Int? = null

    val name = MutableLiveData<String>()
    val address = MutableLiveData<String?>(null)
    val startDate = MutableLiveData<String?>(null)
    val endDate = MutableLiveData<String?>(null)
    val description = MutableLiveData<String?>()
    val type = MutableLiveData<String>()

    val submitLoading = LoadingMutableLiveData()

    protected var shouldValidateDates = true
    protected var startDateTime: DateTime? = null
    protected var endDateTime: DateTime? = null

    val validateName = name.switchMapSuspend {
        validateName(it)
    }

    val validateAddress = address.switchMapSuspend {
        if (it.isNullOrBlank()) {
            placeName = null
        }
        validateAddress(it)
    }

    val validateDates = PairMediatorLiveData(startDate, endDate).switchMapSuspend { (start, end) ->
        validateDates(start, end)
    }

    open suspend fun validateName(name: String): Int {
        return validator.validateIfNotEmpty(name)
    }

    open suspend fun validateAddress(address: String?): Int {
        address ?: return OK
        return validator.validateIfNotTooLong(address, Constants.OTHER_MAX_LEN)
    }

    open suspend fun validateDates(startDate: String?, endDate: String?, type: ValidateDates = NORMAL): Int {
        if (!shouldValidateDates) {
            return OK
        }
        if (startDate.isNullOrBlank()) {
            startDateTime = null
        }
        if (endDate.isNullOrBlank()) {
            endDateTime = null
        }
        return validator.validateDates(startDateTime?.millis, endDateTime?.millis, type)
    }

    open val enableSubmit = ValidationMediator(
        validateName,
        validateAddress,
        validateDates
    )

    open fun find() {
        val whatToSearch = placeName ?: address.value
        findAccommodationEvent.value = whatToSearch
    }

    open fun placeFound(place: Place) {
        placeId = place.id
        placeName = place.name
    }

    fun selectType(position: Int) {
        selectedTypePosition = position
    }

    fun startTimePicker() {
        val datePickerInfo = DatePickerInfo(
            initialDate = startDateTime?.millis ?: endDateTime?.millis ?: today
        ) { doNothing }

        val timePickerInfo = TimePickerInfo(
            hour = startDateTime?.hourOfDay ?: endDateTime?.hourOfDay,
            minute = startDateTime?.minuteOfDay ?: endDateTime?.minuteOfDay,
        ) { _, _ -> doNothing }

        showDateAndTimePicker(datePickerInfo, timePickerInfo) {
            viewModelScope.launch {
                startDateTime = it
                startDate.value = it.toNiceString()
            }
        }
    }

    fun endTimePicker() {
        val datePickerInfo = DatePickerInfo(
            initialDate = endDateTime?.millis ?: startDateTime?.millis ?: today
        ) { doNothing }

        val timePickerInfo = TimePickerInfo(
            hour = endDateTime?.hourOfDay ?: startDateTime?.hourOfDay,
            minute = endDateTime?.minuteOfDay ?: startDateTime?.minuteOfDay,
        ) { _, _ -> doNothing }

        showDateAndTimePicker(datePickerInfo, timePickerInfo) {
            viewModelScope.launch {
                endDateTime = it
                endDate.value = it.toNiceString()
            }
        }
    }

    open fun submit(request: Request) {
        viewModelScope.launchIO {
            val result = when (purpose) {
                Purpose.CREATE -> createPoint(request) ?: return@launchIO
                Purpose.EDIT -> TODO()
                null -> return@launchIO
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

    protected open suspend fun createPoint(request: Request): Flow<Result<CreatedResponse>>? {
        return pointRepository.createPoint(tripId ?: return null, request)
    }

    protected fun handleFailure(error: Error) {
        submitLoading.value = false
        unexpectedError(error)
    }

    enum class Purpose {
        CREATE, EDIT
    }
}