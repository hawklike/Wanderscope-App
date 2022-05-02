package cz.cvut.fit.steuejan.wanderscope.app.arch

import android.widget.Toast
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cz.cvut.fit.steuejan.wanderscope.app.event.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent

abstract class BaseViewModel(
    protected open val state: SavedStateHandle? = null
) : ViewModel(), LifecycleObserver {

    val navigateEvent = SingleLiveEvent<NavigationEvent>()
    val toastEvent = SingleLiveEvent<ToastInfo>()

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

    fun <T> setStateData(paramName: String, data: T) {
        state?.set(paramName, data)
    }

    fun <T> getStateData(paramName: String): T? {
        return state?.get<T>(paramName)
    }

    fun <T> getStateLiveData(paramName: String): LiveData<T>? {
        return state?.getLiveData(paramName)
    }

    data class ToastInfo(val message: String, val lenght: Int = Toast.LENGTH_SHORT)
}