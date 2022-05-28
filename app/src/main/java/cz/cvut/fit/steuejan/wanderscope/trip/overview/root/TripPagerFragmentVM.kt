package cz.cvut.fit.steuejan.wanderscope.trip.overview.root

import androidx.lifecycle.SavedStateHandle
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.livedata.AnySingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load

class TripPagerFragmentVM(
    savedStateHandle: SavedStateHandle
) : BaseViewModel(savedStateHandle) {

    val tripOverviewResult = SingleLiveEvent<Load>()
    val tripItineraryResult = AnySingleLiveEvent()

    val pageNumber = getStateLiveData<Int>(PAGE_NUMBER)

    fun savePage(pageNumber: Int) {
        setStateData(PAGE_NUMBER, pageNumber)
    }

    companion object {
        const val PAGE_NUMBER = "pageNumber"
    }
}