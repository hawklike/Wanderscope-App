package cz.cvut.fit.steuejan.wanderscope.auth.api.request

import com.squareup.moshi.Json

data class ForgotPasswordRequest(
    @Json(name = "email")
    val email: String
)