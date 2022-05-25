package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.getDaysHoursAndMinutes
import cz.cvut.fit.steuejan.wanderscope.app.extension.getStartTime
import cz.cvut.fit.steuejan.wanderscope.app.extension.isNow
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.points.transport.model.TransportType
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.TripItineraryItem
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.model.ItineraryType

data class TransportItineraryResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "transport")
    val transport: TransportType,
    @Json(name = "from")
    val from: Address,
    @Json(name = "to")
    val to: Address,
    @Json(name = "duration")
    val duration: Duration
) : ItineraryResponse(ItineraryType.TRANSPORT) {

    override suspend fun toItem(first: Boolean, last: Boolean) = TripItineraryItem(
        id = id,
        name = name,
        type = TripPointType.TRANSPORT,
        icon = transport.toIcon(),
        time = duration.getStartTime(),
        duration = duration.getDaysHoursAndMinutes(),
        address = from.name,
        toAddress = to.name,
        lastItem = last,
        active = isActive()
    )

    override suspend fun isActive(): Boolean {
        return duration.isNow()
    }
}
