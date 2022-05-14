package cz.cvut.fit.steuejan.wanderscope.app.common.data

import com.squareup.moshi.Json

data class Address(
    @Json(name = "googlePlaceId")
    val googlePlaceId: String? = null,
    @Json(name = "address")
    val address: String? = null
)