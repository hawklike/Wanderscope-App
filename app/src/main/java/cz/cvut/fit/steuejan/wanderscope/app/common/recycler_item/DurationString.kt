package cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DurationString(
    val startDate: String,
    val endDate: String
) : Parcelable
