package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.response

import com.squareup.moshi.Json
import org.joda.time.DateTime

data class ExpenseResponse(
    @Json(name = "id")
    val id: Long,
    @Json(name = "tripId")
    val tripId: Int,
    @Json(name = "roomId")
    val roomId: Int,
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
