package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.getNights
import cz.cvut.fit.steuejan.wanderscope.app.extension.getStartTime
import cz.cvut.fit.steuejan.wanderscope.app.extension.isNow
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.model.AccommodationType
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.TripItineraryItem
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.model.ItineraryType

data class AccommodationItineraryResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "accommodation")
    val accommodation: AccommodationType,
    @Json(name = "address")
    val address: Address,
    @Json(name = "duration")
    val duration: Duration,
) : ItineraryResponse(ItineraryType.ACCOMMODATION) {

    override suspend fun toItem(first: Boolean, last: Boolean) = TripItineraryItem(
        id = id,
        name = name,
        type = TripPointType.ACCOMMODATION,
        icon = accommodation.toIcon(),
        time = duration.getStartTime(),
        duration = duration.getNights(),
        address = address.name,
        toAddress = null,
        lastItem = last,
        active = isActive()
    )

    override suspend fun isActive(): Boolean {
        return duration.isNow()
    }
}
