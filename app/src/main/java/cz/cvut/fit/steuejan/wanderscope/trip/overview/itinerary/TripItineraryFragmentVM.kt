package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary

import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response.TripItineraryResponse
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.repository.ItineraryRepository
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentDirections
import timber.log.Timber

class TripItineraryFragmentVM(
    private val itineraryRepository: ItineraryRepository
) : BaseViewModel() {

    fun showItinerary(tripId: Int) {
        viewModelScope.launchIO {
            itineraryRepository.getItinerary(tripId).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> doNothing //todo
                    is Result.Loading -> doNothing //todo
                    is Result.Success -> itinerarySuccess(it.data)
                }
            }
        }
    }

    private fun itinerarySuccess(data: TripItineraryResponse) {
        data.itinerary.forEach {
            Timber.d(it.toString())
        }
    }

    fun click() {
        navigateTo(NavigationEvent.Action(TripPagerFragmentDirections.actionTripPagerFragmentToAccountFragment()))
    }

}