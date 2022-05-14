package cz.cvut.fit.steuejan.wanderscope.points.accommodation.response

import com.squareup.moshi.Json

data class MultipleAccommodationResponse(
    @Json(name = "accommodation")
    val accommodation: List<AccommodationResponse>
)