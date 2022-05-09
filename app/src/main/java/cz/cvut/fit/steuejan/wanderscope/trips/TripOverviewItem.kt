package cz.cvut.fit.steuejan.wanderscope.trips

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.DurationInItem

data class TripOverviewItem(
    val id: Int,
    val name: String,
    val duration: DurationInItem?,
    val days: Int?,
    val imageUrl: String?
) : RecyclerItem {

    override val layoutId = R.layout.item_trip_overview

    override fun isSameItem(other: RecyclerItem): Boolean {
        return other is TripOverviewItem && id == other.id
    }

    override fun hasSameContent(other: RecyclerItem): Boolean {
        return other is TripOverviewItem && this == other
    }
}
