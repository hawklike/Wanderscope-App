package cz.cvut.fit.steuejan.wanderscope.points.common.overview.bundle

import android.os.Parcelable
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.points.TripPointOverviewItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class PointOverviewBundle(
    val tripId: Int,
    val pointId: Int,
    val title: String,
    val userRole: UserRole
) : Parcelable {
    companion object {
        fun create(tripId: Int, userRole: UserRole, item: TripPointOverviewItem) =
            PointOverviewBundle(
                tripId = tripId,
                pointId = item.id,
                title = item.name,
                userRole = userRole
            )
    }
}