package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary

import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentDirections

class TripItineraryFragmentVM : BaseViewModel() {

    fun click() {
        navigateTo(NavigationEvent.Action(TripPagerFragmentDirections.actionTripPagerFragmentToAccountFragment()))
    }

}