package cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class LoadingMediator(
    private vararg val loading: LiveData<Boolean>
) : MediatorLiveData<Boolean>() {

    init {
        loading.forEach { liveData ->
            addSource(liveData) {
                value = it || othersValid(liveData)
            }
        }
    }

    private fun othersValid(currentLiveData: LiveData<Boolean>): Boolean {
        return loading
            .filterNot { it == currentLiveData }
            .any { it.value == true }
    }
}