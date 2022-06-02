package cz.cvut.fit.steuejan.wanderscope.account.api.request

import com.squareup.moshi.Json

data class ChangeDisplayNameRequest(
    @Json(name = "displayName")
    val displayName: String?
)
