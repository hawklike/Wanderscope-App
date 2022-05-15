package cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Contact
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.model.AccommodationType
import cz.cvut.fit.steuejan.wanderscope.points.common.AbstractTripPointResponse


data class AccommodationResponse(
    @Json(name = "id")
    override val id: Int,
    @Json(name = "tripId")
    override val tripId: Int,
    @Json(name = "duration")
    override val duration: Duration,
    @Json(name = "name")
    override val name: String,
    @Json(name = "address")
    val address: Address,
    @Json(name = "contact")
    val contact: Contact,
    @Json(name = "type")
    override val type: AccommodationType,
    @Json(name = "description")
    val description: String?
) : AbstractTripPointResponse()