package cz.cvut.fit.steuejan.wanderscope.points.transport.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Coordinates
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import cz.cvut.fit.steuejan.wanderscope.points.transport.model.TransportType

data class TransportResponse(
    @Json(name = "id")
    override val id: Int,
    @Json(name = "tripId")
    override val tripId: Int,
    @Json(name = "name")
    override val name: String,
    @Json(name = "duration")
    override val duration: Duration,
    @Json(name = "type")
    override val type: TransportType,
    @Json(name = "from")
    override val address: Address,
    @Json(name = "to")
    val to: Address,
    @Json(name = "description")
    override val description: String?,
    @Json(name = "cars")
    val cars: List<String>?,
    @Json(name = "seats")
    val seats: List<String>?,
    @Json(name = "fromCoordinates")
    override val coordinates: Coordinates,
    @Json(name = "toCoordinates")
    val toCoordinates: Coordinates
) : PointResponse()