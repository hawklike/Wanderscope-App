package cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item

import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem

open class EmptyItem(
    @StringRes val title: Int,
    @DrawableRes val image: Int = R.drawable.image_not_found,
    @StringRes val subtitle: Int? = null,
    @DimenRes val marginTop: Int = R.dimen.margin_0,
    @DimenRes val marginBottom: Int = R.dimen.app_recycler_item_bottom_margin,
    @DimenRes val marginStart: Int = R.dimen.margin_0,
    @DimenRes val marginEnd: Int = R.dimen.margin_0,
    @StringRes val buttonText: Int? = null
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
            R.drawable.image_place_not_found,
            R.string.no_past_trips_subtitle
        )

        fun accommodation() = EmptyItem(
            R.string.accommodation_empty,
            R.drawable.image_accommodation_not_found,
            marginEnd = R.dimen.app_layout_main_horizontal_padding
        )

        fun activities() = EmptyItem(
            R.string.activities_empty,
            R.drawable.image_activity_not_found,
            marginEnd = R.dimen.app_layout_main_horizontal_padding
        )

        fun transport() = EmptyItem(
            R.string.transport_empty,
            R.drawable.image_transport_not_found,
            marginEnd = R.dimen.app_layout_main_horizontal_padding
        )

        fun places() = EmptyItem(
            R.string.places_empty,
            R.drawable.image_place_not_found,
            marginEnd = R.dimen.app_layout_main_horizontal_padding
        )

        fun documents() = EmptyItem(
            R.string.documents_empty,
            R.drawable.image_document_not_found,
            marginEnd = R.dimen.app_layout_main_horizontal_padding
        )

        fun itinerary() = EmptyItem(
            R.string.itinerary_empty,
            R.drawable.image_not_found,
            marginTop = R.dimen.app_first_item_overview_margin
        )

        fun expenseRooms() = EmptyItem(
            R.string.expense_rooms_empty,
            R.drawable.image_expenses_empty,
            R.string.expense_rooms_empty_subtitle,
            marginTop = R.dimen.app_first_item_overview_margin
        )
    }
}
