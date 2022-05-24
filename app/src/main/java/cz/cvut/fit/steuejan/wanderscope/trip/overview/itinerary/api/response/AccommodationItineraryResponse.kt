package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.getStartTime
import cz.cvut.fit.steuejan.wanderscope.app.extension.toDurationString
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.model.AccommodationType
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.TripItineraryItem
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.model.ItineraryType
import org.joda.time.format.DateTimeFormat

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

    override suspend fun toItem() = TripItineraryItem(
        id = id,
        name = name,
        type = TripPointType.ACCOMMODATION,
        icon = accommodation.toIcon(),
        tint = R.color.colorAccommodation,
        time = duration.getStartTime(),
        duration = duration.toDurationString(DateTimeFormat.shortDateTime()),
        address = address.name,
        toAddress = null
    )
}
