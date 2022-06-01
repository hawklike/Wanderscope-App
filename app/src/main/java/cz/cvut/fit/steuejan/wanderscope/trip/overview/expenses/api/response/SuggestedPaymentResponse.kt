package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.response

import com.squareup.moshi.Json

data class SuggestedPaymentResponse(
    @Json(name = "from")
    val from: String,
    @Json(name = "to")
    val to: String,
    @Json(name = "amountInCents")
    val amountInCents: Long
)
