package cz.cvut.fit.steuejan.wanderscope.auth.api.response

import com.squareup.moshi.Json

data class AuthResponse(
    @Json(name = "accessToken")
    val accessToken: String,
    @Json(name = "refreshToken")
    val refreshToken: String
)
