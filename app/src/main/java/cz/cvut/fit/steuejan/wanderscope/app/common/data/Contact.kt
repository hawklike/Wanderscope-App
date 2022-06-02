package cz.cvut.fit.steuejan.wanderscope.app.common.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    @Json(name = "phone")
    val phone: String? = null,
    @Json(name = "email")
    val email: String? = null,
    @Json(name = "website")
    val website: String? = null
) : Parcelable