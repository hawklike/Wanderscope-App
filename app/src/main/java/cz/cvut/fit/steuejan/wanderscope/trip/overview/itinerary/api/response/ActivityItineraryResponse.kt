package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.getDaysHoursAndMinutes
import cz.cvut.fit.steuejan.wanderscope.app.extension.getStartTime
import cz.cvut.fit.steuejan.wanderscope.app.extension.isNow
import cz.cvut.fit.steuejan.wanderscope.points.activity.model.ActivityType
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.TripItineraryItem
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.model.ItineraryType

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

    override suspend fun toItem(first: Boolean, last: Boolean) = TripItineraryItem(
        id = id,
        name = name,
        type = TripPointType.ACTIVITY,
        icon = activity.toIcon(),
        time = duration.getStartTime(),
        duration = duration.getDaysHoursAndMinutes(),
        address = address.name,
        toAddress = null,
        lastItem = last,
        active = isActive()
    )

    override suspend fun isActive(): Boolean {
        return duration.isNow()
    }
}
