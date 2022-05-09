package cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem

data class EmptyItem(
    @StringRes val title: Int,
    @StringRes val subtitle: Int? = null,
    @DrawableRes val image: Int = R.drawable.image_not_found,
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
            R.string.no_upcoming_trips_subtitle,
            R.drawable.image_not_found
        )

        fun pastTrips() = EmptyItem(
            R.string.no_past_trips_title,
            R.string.no_past_trips_subtitle,
            R.drawable.image_add_trip
        )
    }
}
