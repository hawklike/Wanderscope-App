package cz.cvut.fit.steuejan.wanderscope.app.arch

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator
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

    fun navigateTo(event: NavigationEvent, onBackground: Boolean = false) {
        if (onBackground) {
            navigateEvent.postValue(event)
        } else {
            navigateEvent.value = event
        }
    }

    fun showToast(toast: ToastInfo, onBackground: Boolean = false) {
        if (onBackground) {
            toastEvent.postValue(toast)
        } else {
            toastEvent.value = toast
        }
    }

    fun showSnackbar(snackbar: SnackbarInfo, onBackground: Boolean = false) {
        if (onBackground) {
            snackbarEvent.postValue(snackbar)
        } else {
            snackbarEvent.value = snackbar
        }
    }

    fun <T> setStateData(paramName: String, data: T) {
        state?.set(paramName, data)
    }

    fun <T> getStateData(paramName: String): T? {
        return state?.get<T>(paramName)
    }

    fun <T> getStateLiveData(paramName: String): LiveData<T>? {
        return state?.getLiveData(paramName)
    }

    data class ToastInfo(@StringRes val message: Int, val lenght: Int = Toast.LENGTH_SHORT)
    data class SnackbarInfo(@StringRes val message: Int, val length: Int = Snackbar.LENGTH_SHORT)
}