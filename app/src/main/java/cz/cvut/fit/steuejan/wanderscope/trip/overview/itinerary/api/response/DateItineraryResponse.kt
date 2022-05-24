package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.TripItineraryItemDate
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.model.ItineraryType
import org.joda.time.format.DateTimeFormat

data class DateItineraryResponse(
    @Json(name = "duration")
    val duration: Duration
) : ItineraryResponse(ItineraryType.DATE) {

    override suspend fun toItem() = TripItineraryItemDate(
        duration.startDate?.toString(DateTimeFormat.mediumDate())
    )
}
