package cz.cvut.fit.steuejan.wanderscope.app.retrofit.response

import com.squareup.moshi.Json

data class CreatedResponse(
    @Json(name = "id")
    val id: Long
)