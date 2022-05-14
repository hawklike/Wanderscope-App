package cz.cvut.fit.steuejan.wanderscope.app.common.data

import com.squareup.moshi.Json

data class Coordinates(
    @Json(name = "longitude")
    val longitude: String? = null,
    @Json(name = "latitude")
    val latitude: String? = null
)