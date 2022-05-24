package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.getStartTime
import cz.cvut.fit.steuejan.wanderscope.app.extension.toDurationString
import cz.cvut.fit.steuejan.wanderscope.points.activity.model.ActivityType
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.TripItineraryItem
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.model.ItineraryType
import org.joda.time.format.DateTimeFormat

data class ActivityItineraryResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "activity")
    val activity: ActivityType,
    @Json(name = "duration")
    val duration: Duration,
    @Json(name = "address")
    val address: Address
) : ItineraryResponse(ItineraryType.ACTIVITY) {

    override suspend fun toItem() = TripItineraryItem(
        id = id,
        name = name,
        type = TripPointType.ACTIVITY,
        icon = activity.toIcon(),
        tint = R.color.colorActivity,
        time = duration.getStartTime(),
        duration = duration.toDurationString(DateTimeFormat.shortDateTime()),
        address = address.name,
        toAddress = null
    )
}
