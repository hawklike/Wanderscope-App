package cz.cvut.fit.steuejan.wanderscope.trips

import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.getDays
import cz.cvut.fit.steuejan.wanderscope.app.extension.toDurationItem
import org.joda.time.DateTime

class TripsFragmentVM : BaseViewModel() {

    val upcomingTrips = MutableLiveData<List<TripOverviewItem>>()

    val pastTrips = MutableLiveData<List<TripOverviewItem>>(emptyList())

    init {
        val trips = mutableListOf<TripOverviewItem>()
        repeat(10) {
            val date = Duration(DateTime.now(), DateTime.now().plusDays(it))
            trips.add(TripOverviewItem(1, "Portugalsko a Španelsko 2021", date.toDurationItem(), date.getDays(), null))
        }
        upcomingTrips.value = trips
    }

}