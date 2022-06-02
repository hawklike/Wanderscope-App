package cz.cvut.fit.steuejan.wanderscope.app.arch

import androidx.lifecycle.MutableLiveData

interface SharedViewModel {
    val sharedData: MutableLiveData<Any?>
}