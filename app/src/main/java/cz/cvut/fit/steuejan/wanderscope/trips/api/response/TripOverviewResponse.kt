package cz.cvut.fit.steuejan.wanderscope.trips.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.extension.getDays
import cz.cvut.fit.steuejan.wanderscope.app.extension.toDurationItem
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.trips.TripOverviewItem

data class TripOverviewResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "role")
    val role: UserRole,
    @Json(name = "duration")
    val duration: Duration,
    @Json(name = "imageUrl")
    val imageUrl: String?
) {
    suspend fun toItem() = withDefault {
        TripOverviewItem(id, name, duration.toDurationItem(), duration.getDays(), imageUrl)
    }
}
