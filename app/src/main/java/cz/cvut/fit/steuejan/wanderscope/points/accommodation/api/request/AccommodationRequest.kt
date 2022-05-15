package cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.request

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Contact
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.model.AccommodationType

data class AccommodationRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "duration")
    val duration: Duration?,
    @Json(name = "type")
    val type: AccommodationType,
    @Json(name = "address")
    val address: Address?,
    @Json(name = "contact")
    val contact: Contact?,
    @Json(name = "description")
    val description: String?
)
