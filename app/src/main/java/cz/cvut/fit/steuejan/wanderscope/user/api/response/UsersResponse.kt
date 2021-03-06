package cz.cvut.fit.steuejan.wanderscope.user.api.response

import com.squareup.moshi.Json

data class UsersResponse(
    @Json(name = "users")
    val users: List<UserResponse>
)
