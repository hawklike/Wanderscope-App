package cz.cvut.fit.steuejan.wanderscope.trips

import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration

class TripsFragmentVM : BaseViewModel() {

    val upcomingTrips = MutableLiveData<List<TripOverviewItem>>()

    init {
        val trips = mutableListOf<TripOverviewItem>()
        repeat(10) {
            trips.add(TripOverviewItem(1, "Portugalsko a Španelsko 2021", Duration(), null))
        }
        upcomingTrips.value = trips
    }

}