package cz.cvut.fit.steuejan.wanderscope.trip.api.response.users

import com.squareup.moshi.Json

data class TripUsersResponse(
    @Json(name = "users")
    val users: List<TripUserResponse>
)
