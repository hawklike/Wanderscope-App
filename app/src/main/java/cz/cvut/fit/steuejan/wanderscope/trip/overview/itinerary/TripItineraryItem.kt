package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary

import androidx.annotation.DrawableRes
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.util.model.FullDuration
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType

data class TripItineraryItem(
    val id: Int,
    val name: String,
    val type: TripPointType,
    @DrawableRes val icon: Int,
    val time: String?,
    val duration: FullDuration?,
    val address: String?,
    val toAddress: String?,
    val lastItem: Boolean,
    val active: Boolean
) : RecyclerItem {

    override val layoutId = R.layout.item_itinerary

    override fun isSameItem(other: RecyclerItem): Boolean {
        return other is TripItineraryItem && id == other.id && type == other.type
    }

    override fun hasSameContent(other: RecyclerItem): Boolean {
        return other is TripItineraryItem &&
                name == other.name &&
                time == other.time &&
                duration == other.duration &&
                icon == other.icon &&
                address == other.address &&
                toAddress == other.toAddress &&
                lastItem == other.lastItem
    }
}
