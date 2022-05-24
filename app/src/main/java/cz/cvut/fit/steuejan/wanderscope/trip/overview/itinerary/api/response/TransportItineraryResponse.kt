package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.points.transport.model.TransportType
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
) : ItineraryResponse(ItineraryType.TRANSPORT)
