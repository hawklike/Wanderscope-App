package cz.cvut.fit.steuejan.wanderscope.app.retrofit.response

import com.squareup.moshi.Json

data class ErrorResponse(
    @Json(name = "status")
    val status: Status,
    @Json(name = "message")
    val message: String,
    @Json(name = "code")
    val customCode: Int? = null
)

open class Error(
    val httpCode: Int,
    val reason: ErrorResponse?
)

object UnspecifiedError : Error(500, null)