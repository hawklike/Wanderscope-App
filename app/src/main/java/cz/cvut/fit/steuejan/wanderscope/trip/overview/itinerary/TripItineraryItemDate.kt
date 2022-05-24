package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem

data class TripItineraryItemDate(
    val date: String?
) : RecyclerItem {

    override val layoutId = R.layout.item_itinerary_date

    override fun isSameItem(other: RecyclerItem): Boolean {
        return other is TripItineraryItemDate && other.date == date
    }

    override fun hasSameContent(other: RecyclerItem): Boolean {
        return isSameItem(other)
    }
}
