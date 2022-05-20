package cz.cvut.fit.steuejan.wanderscope.points.common.api.response

import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Coordinates
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.toNiceString
import cz.cvut.fit.steuejan.wanderscope.points.TripPointOverviewItem
import cz.cvut.fit.steuejan.wanderscope.points.common.PointType

abstract class PointResponse {
    abstract val id: Int
    abstract val tripId: Int
    abstract val duration: Duration
    abstract val name: String
    abstract val type: PointType
    abstract val description: String?
    abstract val coordinates: Coordinates
    abstract val address: Address

    open suspend fun toOverviewItem() = TripPointOverviewItem(
        id,
        name,
        duration.startDate?.toNiceString(),
        duration.endDate?.toNiceString(),
        type.toIcon()
    )
}