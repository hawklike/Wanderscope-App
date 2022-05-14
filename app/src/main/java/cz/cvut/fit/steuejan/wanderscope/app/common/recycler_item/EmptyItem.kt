package cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem

data class EmptyItem(
    @StringRes val title: Int,
    @DrawableRes val image: Int = R.drawable.image_not_found,
    @StringRes val subtitle: Int? = null,
) : RecyclerItem {

    override val layoutId = R.layout.item_empty

    override fun isSameItem(other: RecyclerItem): Boolean {
        return other is EmptyItem && this == other
    }

    override fun hasSameContent(other: RecyclerItem): Boolean {
        return other is EmptyItem && this == other
    }

    companion object {
        fun upcomingTrips() = EmptyItem(
            R.string.no_upcoming_trips_title,
            R.drawable.image_not_found,
            R.string.no_upcoming_trips_subtitle
        )

        fun pastTrips() = EmptyItem(
            R.string.no_past_trips_title,
            R.drawable.image_been_nowhere,
            R.string.no_past_trips_subtitle
        )

        fun accommodation() = EmptyItem(
            R.string.accommodation_empty,
            R.drawable.image_accommodation_not_found
        )

        fun activities() = EmptyItem(
            R.string.activities_empty,
            R.drawable.image_activity_not_found
        )

        fun transport() = EmptyItem(
            R.string.transport_empty,
            R.drawable.image_transport_not_found
        )

        fun places() = EmptyItem(
            R.string.places_empty,
            R.drawable.image_been_nowhere
        )

        fun documents() = EmptyItem(
            R.string.documents_empty,
            R.drawable.image_document_not_found
        )
    }
}
