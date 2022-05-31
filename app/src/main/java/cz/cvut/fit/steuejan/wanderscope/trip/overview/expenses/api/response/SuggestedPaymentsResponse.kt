package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.response

import com.squareup.moshi.Json

data class SuggestedPaymentsResponse(
    @Json(name = "payments")
    val payments: List<SuggestedPaymentResponse>
)
