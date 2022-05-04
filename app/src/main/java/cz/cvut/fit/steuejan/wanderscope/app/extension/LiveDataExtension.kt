package cz.cvut.fit.steuejan.wanderscope.app.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap

inline fun <X, Y> LiveData<X>.switchMapSuspend(
    crossinline transform: suspend (X) -> Y
): LiveData<Y> = switchMap {
    liveData {
        emit(transform.invoke(it))
    }
}