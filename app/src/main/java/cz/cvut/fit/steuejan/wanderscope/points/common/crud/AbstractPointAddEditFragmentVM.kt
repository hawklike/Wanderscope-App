package cz.cvut.fit.steuejan.wanderscope.points.common.crud

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.DatePickerInfo.Companion.today
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.extension.toNiceString
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.livedata.mediator.PairMediatorLiveData
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.trip.crud.AddEditTripFragmentVM
import kotlinx.coroutines.launch
import org.joda.time.DateTime

abstract class AbstractPointAddEditFragmentVM(@StringRes titleRes: Int) : BaseViewModel() {

    val title = MutableLiveData<Int>()

    private var purpose: AddEditTripFragmentVM.Purpose? = null

    init {
        title.value = titleRes
        purpose = AddEditTripFragmentVM.Purpose.CREATE
    }

    val findAccommodationEvent = SingleLiveEvent<String?>()

    val search = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val address = MutableLiveData<String?>(null)
    val startDate = MutableLiveData<String?>(null)
    val endDate = MutableLiveData<String?>(null)
    val description = MutableLiveData<String?>()

    protected var shouldValidateDates = true
    protected var startDateTime: DateTime? = null
    protected var endDateTime: DateTime? = null

    val validateName = name.switchMapSuspend {
        validateName(it)
    }

    val validateAddress = address.switchMapSuspend {
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

    open suspend fun validateDates(startDate: String?, endDate: String?): Int {
        if (!shouldValidateDates) {
            return OK
        }
        if (startDate.isNullOrBlank()) {
            startDateTime = null
        }
        if (endDate.isNullOrBlank()) {
            endDateTime = null
        }
        return validator.validateDates(startDateTime?.millis, endDateTime?.millis)
    }

    open val enableSubmit = ValidationMediator(
        validateName,
        validateAddress,
        validateDates
    )

    open fun find() {
        findAccommodationEvent.value = search.value
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

    abstract fun placeFound(place: Place)

    enum class Purpose {
        CREATE, EDIT
    }

}