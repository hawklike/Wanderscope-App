package cz.cvut.fit.steuejan.wanderscope.account.api.request

import com.squareup.moshi.Json

data class ChangePasswordRequest(
    @Json(name = "oldPassword")
    val oldPassword: String,
    @Json(name = "newPassword")
    val newPassword: String,
    @Json(name = "confirmNewPassword")
    val confirmNewPassword: String
)
