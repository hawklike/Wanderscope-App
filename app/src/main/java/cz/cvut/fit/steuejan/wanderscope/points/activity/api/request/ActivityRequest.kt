package cz.cvut.fit.steuejan.wanderscope.points.activity.api.request

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Coordinates
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.points.activity.model.ActivityType
import cz.cvut.fit.steuejan.wanderscope.points.common.api.request.PointRequest

data class ActivityRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "duration")
    val duration: Duration?,
    @Json(name = "type")
    val type: ActivityType,
    @Json(name = "address")
    val address: Address?,
    @Json(name = "mapLink")
    val mapLink: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "website")
    val website: String?,
    @Json(name = "coordinates")
    val coordinates: Coordinates?
) : PointRequest