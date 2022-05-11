package cz.cvut.fit.steuejan.wanderscope.trips

import android.os.Parcelable
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.DurationInItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TripOverviewItem(
    val id: Int,
    val name: String,
    val duration: DurationInItem?,
    val days: Int?,
    val imageUrl: String?,
    val userRole: UserRole,
    val description: String?
) : RecyclerItem, Parcelable {

    @IgnoredOnParcel
    override val layoutId = R.layout.item_trip_overview

    override fun isSameItem(other: RecyclerItem): Boolean {
        return other is TripOverviewItem && id == other.id
    }

    override fun hasSameContent(other: RecyclerItem): Boolean {
        return other is TripOverviewItem &&
                name == other.name &&
                duration == other.duration &&
                days == other.days &&
                imageUrl == other.imageUrl
    }
}
