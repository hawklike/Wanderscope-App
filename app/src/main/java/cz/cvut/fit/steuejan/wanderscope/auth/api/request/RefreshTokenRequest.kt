package cz.cvut.fit.steuejan.wanderscope.auth.api.request

import com.squareup.moshi.Json

data class RefreshTokenRequest(
    @Json(name = "refreshToken")
    val refreshToken: String
)