package cz.cvut.fit.steuejan.wanderscope.points.common

import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.toNiceString
import cz.cvut.fit.steuejan.wanderscope.trip.overview.TripPointOverviewItem

abstract class AbstractTripPointResponse {
    abstract val id: Int
    abstract val tripId: Int
    abstract val duration: Duration
    abstract val name: String
    abstract val type: PointType

    open suspend fun toOverviewItem() = TripPointOverviewItem(
        id,
        name,
        duration.startDate?.toNiceString(),
        duration.endDate?.toNiceString(),
        type.toIcon()
    )
}