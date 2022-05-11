package cz.cvut.fit.steuejan.wanderscope.trip.overview

import androidx.annotation.DrawableRes
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem

data class TripPointOverviewItem(
    val id: Int,
    val name: String,
    val startDate: String?,
    val endDate: String?,
    @DrawableRes val icon: Int
) : RecyclerItem {

    override val layoutId = R.layout.item_trip_point_overview

    override fun isSameItem(other: RecyclerItem): Boolean {
        return other is TripPointOverviewItem && id == other.id
    }

    override fun hasSameContent(other: RecyclerItem): Boolean {
        return other is TripPointOverviewItem && this == other
    }
}
