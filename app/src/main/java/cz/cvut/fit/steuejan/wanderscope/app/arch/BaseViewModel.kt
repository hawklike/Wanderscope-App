package cz.cvut.fit.steuejan.wanderscope.app.arch

import android.widget.Toast
import androidx.annotation.*
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber


abstract class BaseViewModel(
    protected open val state: SavedStateHandle? = null
) : ViewModel(), LifecycleObserver, KoinComponent {

    protected val validator: InputValidator by inject()

    val navigateEvent = SingleLiveEvent<NavigationEvent>()
    val toastEvent = SingleLiveEvent<ToastInfo>()
    val snackbarEvent = SingleLiveEvent<SnackbarInfo>()
    val showLoading = SingleLiveEvent<Boolean>()
    val datePickerEvent = SingleLiveEvent<DatePickerInfo>()
    val timePickerEvent = SingleLiveEvent<TimePickerInfo>()

    protected fun navigateTo(event: NavigationEvent, onBackground: Boolean = false) {
        if (onBackground) {
            navigateEvent.postValue(event)
        } else {
            navigateEvent.value = event
        }
    }

    protected fun showToast(toast: ToastInfo, onBackground: Boolean = false) {
        if (onBackground) {
            toastEvent.postValue(toast)
        } else {
            toastEvent.value = toast
        }
    }

    protected fun showSnackbar(snackbar: SnackbarInfo, onBackground: Boolean = false) {
        if (onBackground) {
            snackbarEvent.postValue(snackbar)
        } else {
            snackbarEvent.value = snackbar
        }
    }

    protected fun showLoading(onBackground: Boolean = false) {
        if (onBackground) {
            showLoading.postValue(true)
        } else {
            showLoading.value = true
        }
    }

    protected fun hideLoading(onBackground: Boolean = false) {
        if (onBackground) {
            showLoading.postValue(false)
        } else {
            showLoading.value = false
        }
    }

    protected fun showDatePicker(dateInfo: DatePickerInfo, onBackground: Boolean = false) {
        if (onBackground) {
            datePickerEvent.postValue(dateInfo)
        } else {
            datePickerEvent.value = dateInfo
        }
    }

    protected fun showTimePicker(timeInfo: TimePickerInfo, onBackground: Boolean = false) {
        if (onBackground) {
            timePickerEvent.postValue(timeInfo)
        } else {
            timePickerEvent.value = timeInfo
        }
    }

    protected fun showDateAndTimePicker(
        dateInfo: DatePickerInfo,
        timeInfo: TimePickerInfo,
        onBackground: Boolean = false,
        onResult: (DateTime) -> Unit
    ) {
        val dateInfoCopy = dateInfo.copy(onPickedDate = {
            val dateTime = DateTime(it, DateTimeZone.UTC)
            val timeInfoCopy = timeInfo.copy(onPickerTime = { hour, minute ->
                onResult.invoke(dateTime.withHourOfDay(hour).withMinuteOfHour(minute))
            })
            showTimePicker(timeInfoCopy, onBackground)
        })
        showDatePicker(dateInfoCopy, onBackground)
    }

    protected open fun unexpectedError(error: Error? = null, retry: () -> Unit = {}) {
        error?.reason?.let {
            Timber.e(it.message)
        }

        showSnackbar(
            SnackbarInfo(
                R.string.unexpected_error,
                backendMessage = error?.reason?.message,
                length = Constants.UNEXPECTED_ERROR_SNACKBAR_LENGTH,
                action = { retry.invoke() }
            )
        )
    }

    @MainThread
    protected fun <T> setStateData(paramName: String, data: T) {
        state?.set(paramName, data)
    }

    @MainThread
    protected fun <T> getStateData(paramName: String): T? {
        return state?.get<T>(paramName)
    }

    @MainThread
    protected fun <T> getStateLiveData(paramName: String): LiveData<T>? {
        return state?.getLiveData(paramName)
    }

    data class ToastInfo(@StringRes val message: Int, val lenght: Int = Toast.LENGTH_SHORT)

    data class SnackbarInfo(
        @StringRes val message: Int,
        val backendMessage: String? = null,
        val length: Int = Snackbar.LENGTH_LONG,
        val actionText: Int = android.R.string.ok,
        val action: ((Snackbar) -> Unit)? = null
    )

    data class DatePickerInfo(
        @StringRes val title: Int? = R.string.datepicker_title_default,
        val initialDate: Long? = today,
        val constraints: CalendarConstraints.Builder? = null,
        val customDatePicker: MaterialDatePicker<Long>? = null,
        val onPickedDate: (Long) -> Unit
    ) {
        companion object {
            val today = MaterialDatePicker.todayInUtcMilliseconds()
        }
    }

    data class TimePickerInfo(
        val hour: Int? = null,
        val minute: Int? = null,
        @StringRes val title: Int? = R.string.timepicker_title_default,
        @TimeFormat val timeFormat: Int? = null,
        val customTimePicker: MaterialTimePicker? = null,
        val onPickerTime: (hour: Int, minute: Int) -> Unit
    )

    data class ChipInfo(
        val text: String,
        val isCloseIconVisible: Boolean,
        @DrawableRes val closeIconDrawable: Int = R.drawable.ic_close,
        @ColorRes val backgroundTint: Int = R.color.colorSurface,
        @StyleRes val textAppearance: Int = R.style.TextAppearance_Wanderscope_Regular,
        @ColorRes val textColor: Int = R.color.colorText,
        @ColorRes val closeIconTint: Int = R.color.colorTextInputIcon,
        val minHeight: Float = 96f
    )
}