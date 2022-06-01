package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.response

import com.squareup.moshi.Json

data class ExpenseRoomsResponse(
    @Json(name = "rooms")
    val rooms: List<ExpenseRoomResponse>
)
