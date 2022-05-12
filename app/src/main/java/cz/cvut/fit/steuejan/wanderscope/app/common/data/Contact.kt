package cz.cvut.fit.steuejan.wanderscope.app.common.data

import com.squareup.moshi.Json

data class Contact(
    @Json(name = "phone")
    val phone: String? = null,
    @Json(name = "email")
    val email: String? = null,
    @Json(name = "website")
    val website: String? = null
)