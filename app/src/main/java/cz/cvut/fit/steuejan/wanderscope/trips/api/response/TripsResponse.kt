package cz.cvut.fit.steuejan.wanderscope.trips.api.response

import com.squareup.moshi.Json

data class TripsResponse(
    @Json(name = "trips")
    val trips: List<TripOverviewResponse>
)
