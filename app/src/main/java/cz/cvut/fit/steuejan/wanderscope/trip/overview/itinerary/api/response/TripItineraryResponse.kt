package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response

import com.squareup.moshi.Json

data class TripItineraryResponse(
    @Json(name = "itinerary")
    val itinerary: List<ItineraryResponse>
)
