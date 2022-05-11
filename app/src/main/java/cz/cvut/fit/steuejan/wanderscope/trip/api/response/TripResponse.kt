package cz.cvut.fit.steuejan.wanderscope.trip.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole

data class TripResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "ownerId")
    val ownerId: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "duration")
    val duration: Duration,
    @Json(name = "description")
    val description: String?,
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "userRole")
    val userRole: UserRole
)
