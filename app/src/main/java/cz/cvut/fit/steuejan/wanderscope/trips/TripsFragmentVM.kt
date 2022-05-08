package cz.cvut.fit.steuejan.wanderscope.trips

import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel

class TripsFragmentVM : BaseViewModel() {

    val text = MutableLiveData<String>()

    fun click() {
        text.value = "ahoj svete"
    }

}