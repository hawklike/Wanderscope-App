package cz.cvut.fit.steuejan.wanderscope.trip.users.api.request

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole

data class ChangeRoleRequest(
    @Json(name = "whomId")
    val userToChangeId: Int,
    @Json(name = "newRole")
    val newRole: UserRole
)
