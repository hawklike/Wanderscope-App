package cz.cvut.fit.steuejan.wanderscope.points.common.overview.bundle

import android.os.Parcelable
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.points.TripPointOverviewItem
import cz.cvut.fit.steuejan.wanderscope.trip.api.response.TripResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class PointOverviewBundle(
    val tripId: Int,
    val pointId: Int,
    val title: String,
    val userRole: UserRole
) : Parcelable {
    companion object {
        fun create(trip: TripResponse, item: TripPointOverviewItem) =
            PointOverviewBundle(
                tripId = trip.id,
                pointId = item.id,
                title = item.name,
                userRole = trip.userRole
            )
    }
}