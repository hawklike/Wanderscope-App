package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.getStartTime
import cz.cvut.fit.steuejan.wanderscope.app.extension.toDurationString
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.points.transport.model.TransportType
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.TripItineraryItem
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.model.ItineraryType
import org.joda.time.format.DateTimeFormat

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

    override suspend fun toItem() = TripItineraryItem(
        id = id,
        name = name,
        type = TripPointType.TRANSPORT,
        icon = transport.toIcon(),
        tint = R.color.colorTransport,
        time = duration.getStartTime(),
        duration = duration.toDurationString(DateTimeFormat.shortDateTime()),
        address = from.name,
        toAddress = to.name
    )
}
