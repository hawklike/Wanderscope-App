package cz.cvut.fit.steuejan.wanderscope.points.place.api.request

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Contact
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Coordinates
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.points.common.api.request.PointRequest
import cz.cvut.fit.steuejan.wanderscope.points.place.model.PlaceType

data class PlaceRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "duration")
    val duration: Duration?,
    @Json(name = "type")
    val type: PlaceType,
    @Json(name = "address")
    val address: Address?,
    @Json(name = "contact")
    val contact: Contact?,
    @Json(name = "coordinates")
    val coordinates: Coordinates?,
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "description")
    val description: String?,
) : PointRequest