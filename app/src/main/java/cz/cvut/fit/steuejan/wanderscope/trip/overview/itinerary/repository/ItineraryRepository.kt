package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.ItineraryApi
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response.TripItineraryResponse
import kotlinx.coroutines.flow.Flow

class ItineraryRepository(private val itineraryApi: ItineraryApi) {

    suspend fun getItinerary(tripId: Int): Flow<Result<TripItineraryResponse>> {
        return performCall { itineraryApi.getItinerary(tripId) }
    }
}