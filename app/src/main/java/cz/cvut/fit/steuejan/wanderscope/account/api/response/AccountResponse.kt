package cz.cvut.fit.steuejan.wanderscope.account.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.AccountType

data class AccountResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "username")
    val username: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "accountType")
    val accountType: AccountType,
    @Json(name = "displayName")
    val displayName: String?
)

