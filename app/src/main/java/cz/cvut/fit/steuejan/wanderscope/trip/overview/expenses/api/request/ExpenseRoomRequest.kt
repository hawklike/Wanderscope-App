package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.request

import com.squareup.moshi.Json

data class ExpenseRoomRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "currency")
    val currency: String,
    @Json(name = "persons")
    val persons: List<String>
)
