package cz.cvut.fit.steuejan.wanderscope.user.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.AccountType
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.util.getName
import cz.cvut.fit.steuejan.wanderscope.user.UserItem

data class UserResponse(
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
) {
    fun toItem(hasMenu: Boolean) = UserItem(
        id,
        getName(username, displayName),
        role,
        hasMenu
    )
}
