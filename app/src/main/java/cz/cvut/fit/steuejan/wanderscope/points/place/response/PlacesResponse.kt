package cz.cvut.fit.steuejan.wanderscope.points.place.response

import com.squareup.moshi.Json

data class PlacesResponse(
    @Json(name = "places")
    val places: List<PlaceResponse>
)
