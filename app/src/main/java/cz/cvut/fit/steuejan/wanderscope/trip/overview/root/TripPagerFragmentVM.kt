package cz.cvut.fit.steuejan.wanderscope.trip.overview.root

import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.livedata.AnySingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load

class TripPagerFragmentVM : BaseViewModel() {
    val tripOverviewResult = SingleLiveEvent<Load>()
    val tripItineraryResult = AnySingleLiveEvent()
}