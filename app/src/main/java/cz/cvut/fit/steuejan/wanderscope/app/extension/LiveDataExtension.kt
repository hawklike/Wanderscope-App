package cz.cvut.fit.steuejan.wanderscope.app.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.delay

inline fun <T, S> LiveData<T>.switchMapSuspend(
    crossinline transform: suspend (T) -> S
): MutableLiveData<S> = switchMap {
    liveData {
        emit(transform.invoke(it))
    }
} as MutableLiveData<S>

fun <T> LiveData<T>.delayAndReturn(delay: Long = 250): LiveData<T> {
    return switchMapSuspend {
        delay(delay)
        it
    }
}