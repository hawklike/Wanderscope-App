package cz.cvut.fit.steuejan.wanderscope.trip.api.request

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration

data class TripRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "duration")
    val duration: Duration,
    @Json(name = "description")
    val description: String?,
    @Json(name = "imageUrl")
    val imageUrl: String?
)
