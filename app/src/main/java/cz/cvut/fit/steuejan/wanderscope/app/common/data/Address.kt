package cz.cvut.fit.steuejan.wanderscope.app.common.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    @Json(name = "googlePlaceId")
    val googlePlaceId: String? = null,
    @Json(name = "address")
    val name: String? = null
) : Parcelable