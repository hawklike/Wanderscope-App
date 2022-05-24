package cz.cvut.fit.steuejan.wanderscope.points.place.api.response

import android.os.Parcelable
import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Contact
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Coordinates
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import cz.cvut.fit.steuejan.wanderscope.points.place.model.PlaceType
import kotlinx.parcelize.Parcelize

@Parcelize
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
    override val address: Address,
    @Json(name = "contact")
    val contact: Contact,
    @Json(name = "wikiBrief")
    val wikiBrief: String?,
    @Json(name = "wikiBriefCzech")
    val wikiBriefCzech: String?,
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "description")
    override val description: String?,
    @Json(name = "coordinates")
    override val coordinates: Coordinates
) : PointResponse(), Parcelable