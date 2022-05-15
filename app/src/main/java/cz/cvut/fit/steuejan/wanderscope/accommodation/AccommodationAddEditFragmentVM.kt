package cz.cvut.fit.steuejan.wanderscope.accommodation

import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.livedata.AnySingleLiveEvent

class AccommodationAddEditFragmentVM : BaseViewModel() {

    val clickEvent = AnySingleLiveEvent()

    fun click() {
        clickEvent.publish()
    }
}