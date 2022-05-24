package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.getDaysHoursAndMinutes
import cz.cvut.fit.steuejan.wanderscope.app.extension.getStartTime
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.points.place.model.PlaceType
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.TripItineraryItem
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.model.ItineraryType

data class PlaceItineraryResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "place")
    val place: PlaceType,
    @Json(name = "address")
    val address: Address,
    @Json(name = "duration")
    val duration: Duration,
) : ItineraryResponse(ItineraryType.PLACE) {

    override suspend fun toItem() = TripItineraryItem(
        id = id,
        name = name,
        type = TripPointType.PLACE,
        icon = place.toIcon(),
        tint = R.color.colorPlace,
        time = duration.getStartTime(),
        duration = duration.getDaysHoursAndMinutes(),
        address = address.name,
        toAddress = null
    )
}
