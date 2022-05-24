package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.model.AccommodationType
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
) : ItineraryResponse(ItineraryType.ACCOMMODATION)
