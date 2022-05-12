package cz.cvut.fit.steuejan.wanderscope.points.place.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Contact
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.points.common.AbstractTripPointResponse
import cz.cvut.fit.steuejan.wanderscope.points.place.model.PlaceType

data class PlaceResponse(
    @Json(name = "id")
    override val id: Int,
    @Json(name = "tripId")
    override val tripId: Int,
    @Json(name = "duration")
    override val duration: Duration,
    @Json(name = "name")
    override val name: String,
    @Json(name = "type")
    override val type: PlaceType,
    @Json(name = "address")
    val address: Address,
    @Json(name = "contact")
    val contact: Contact,
    @Json(name = "wikiBrief")
    val wikiBrief: String?,
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "description")
    val description: String?
) : AbstractTripPointResponse()