package cz.cvut.fit.steuejan.wanderscope.app.arch

import android.widget.Toast
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
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

    protected fun showDatePicker(initialDate: DatePickerInfo, onBackground: Boolean = false) {
        if (onBackground) {
            datePickerEvent.postValue(initialDate)
        } else {
            datePickerEvent.value = initialDate
        }
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
}