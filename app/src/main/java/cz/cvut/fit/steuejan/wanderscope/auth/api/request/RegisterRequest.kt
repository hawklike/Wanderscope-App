package cz.cvut.fit.steuejan.wanderscope.auth.api.request

import com.squareup.moshi.Json

data class RegisterRequest(
    @Json(name = "username")
    val username: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "confirmPassword")
    val confirmPassword: String
)
