package cz.cvut.fit.steuejan.wanderscope.app.arch

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseViewModel(
    protected open val state: SavedStateHandle? = null
) : ViewModel(), LifecycleObserver, KoinComponent {

    protected val validator: InputValidator by inject()

    val navigateEvent = SingleLiveEvent<NavigationEvent>()
    val toastEvent = SingleLiveEvent<ToastInfo>()
    val snackbarEvent = SingleLiveEvent<SnackbarInfo>()
    val showLoading = SingleLiveEvent<Boolean>()

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

    protected open fun unexpectedError(retry: () -> Unit = {}) {
        showSnackbar(
            SnackbarInfo(
                R.string.unexpected_error,
                length = Constants.UNEXPECTED_ERROR_SNACKBAR_LENGTH,
                action = { retry.invoke() })
        )
    }

    protected fun <T> setStateData(paramName: String, data: T) {
        state?.set(paramName, data)
    }

    protected fun <T> getStateData(paramName: String): T? {
        return state?.get<T>(paramName)
    }

    protected fun <T> getStateLiveData(paramName: String): LiveData<T>? {
        return state?.getLiveData(paramName)
    }

    data class ToastInfo(@StringRes val message: Int, val lenght: Int = Toast.LENGTH_SHORT)

    data class SnackbarInfo(
        @StringRes val message: Int,
        val length: Int = Snackbar.LENGTH_LONG,
        val actionText: Int = android.R.string.ok,
        val action: ((Snackbar) -> Unit)? = null
    )
}