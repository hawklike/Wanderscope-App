package cz.cvut.fit.steuejan.wanderscope.points.transport.api.request

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Coordinates
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.points.common.api.request.PointRequest
import cz.cvut.fit.steuejan.wanderscope.points.transport.model.TransportType

data class TransportRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "duration")
    val duration: Duration?,
    @Json(name = "type")
    val type: TransportType,
    @Json(name = "from")
    val from: Address?,
    @Json(name = "to")
    val to: Address?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "cars")
    val cars: List<String>?,
    @Json(name = "seats")
    val seats: List<String>?,
    @Json(name = "fromCoordinates")
    val fromCoordinates: Coordinates?,
    @Json(name = "toCoordinates")
    val toCoordinates: Coordinates?
) : PointRequest