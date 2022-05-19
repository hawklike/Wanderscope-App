package cz.cvut.fit.steuejan.wanderscope.points.activity.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Coordinates
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.points.activity.model.ActivityType
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse

data class ActivityResponse(
    @Json(name = "id")
    override val id: Int,
    @Json(name = "tripId")
    override val tripId: Int,
    @Json(name = "duration")
    override val duration: Duration,
    @Json(name = "name")
    override val name: String,
    @Json(name = "type")
    override val type: ActivityType,
    @Json(name = "address")
    val address: Address,
    @Json(name = "mapLink")
    val mapLink: String?,
    @Json(name = "description")
    override val description: String?,
    @Json(name = "website")
    val website: String?,
    @Json(name = "coordinates")
    val coordinates: Coordinates
) : PointResponse()