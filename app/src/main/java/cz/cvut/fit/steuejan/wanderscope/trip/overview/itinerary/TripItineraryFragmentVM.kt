package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response.TripItineraryResponse
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.repository.ItineraryRepository

class TripItineraryFragmentVM(
    private val itineraryRepository: ItineraryRepository
) : BaseViewModel() {

    val itinerary = MutableLiveData<List<RecyclerItem>>()

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

    private suspend fun itinerarySuccess(data: TripItineraryResponse) {
        val items = withDefault {
            data.itinerary.map {
                it.toItem()
            }
        }
        itinerary.value = items
    }

}