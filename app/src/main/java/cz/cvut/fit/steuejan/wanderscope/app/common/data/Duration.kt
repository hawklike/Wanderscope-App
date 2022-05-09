package cz.cvut.fit.steuejan.wanderscope.app.common.data

import com.squareup.moshi.Json
import org.joda.time.DateTime

data class Duration(
    @Json(name = "startDate")
    val startDate: DateTime? = null,
    @Json(name = "endDate")
    val endDate: DateTime? = null
)
