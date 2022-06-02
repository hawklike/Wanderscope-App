package cz.cvut.fit.steuejan.wanderscope.trip.users.api.request

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole

data class InviteUserRequest(
    @Json(name = "username")
    val username: String,
    @Json(name = "role")
    val role: UserRole
)
