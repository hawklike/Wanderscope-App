package cz.cvut.fit.steuejan.wanderscope.app.common.data

import android.os.Parcelable
import com.squareup.moshi.Json
import org.joda.time.DateTime

@kotlinx.parcelize.Parcelize
data class Duration(
    @Json(name = "startDate")
    val startDate: DateTime? = null,
    @Json(name = "endDate")
    val endDate: DateTime? = null
) : Parcelable
