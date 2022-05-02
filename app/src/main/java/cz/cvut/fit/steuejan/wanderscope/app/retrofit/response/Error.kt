package cz.cvut.fit.steuejan.wanderscope.app.retrofit.response

import com.squareup.moshi.Json

data class Error(
    @Json(name = "status")
    val status: Status = Status.INTERNAL_ERROR,
    @Json(name = "message")
    val message: String? = null,
    @Json(name = "code")
    val code: Int? = null
)