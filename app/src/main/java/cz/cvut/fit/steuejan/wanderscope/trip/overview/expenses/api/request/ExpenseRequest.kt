package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.request

import com.squareup.moshi.Json
import org.joda.time.DateTime

data class ExpenseRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "amountInCents")
    val amountInCents: Long,
    @Json(name = "whoPaid")
    val whoPaid: String,
    @Json(name = "whoOwes")
    val whoOwes: List<String>,
    @Json(name = "date")
    val date: DateTime?
)
