package cz.cvut.fit.steuejan.wanderscope.trip.api.response.users

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.AccountType
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole

data class TripUserResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "username")
    val username: String,
    @Json(name = "displayName")
    val displayName: String?,
    @Json(name = "email")
    val email: String,
    @Json(name = "accountType")
    val accountType: AccountType,
    @Json(name = "role")
    val role: UserRole
)
