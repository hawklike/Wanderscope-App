package cz.cvut.fit.steuejan.wanderscope.app.common.data

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.util.multipleLet

data class Coordinates(
    @Json(name = "longitude")
    val longitude: String? = null,
    @Json(name = "latitude")
    val latitude: String? = null
) {

    fun toLatLng(): LatLng? {
        val latitude = this.latitude?.toDoubleOrNull()
        val longitude = this.longitude?.toDoubleOrNull()
        return multipleLet(latitude, longitude) { lat, lon ->
            LatLng(lat, lon)
        }
    }

    companion object {
        fun createSafeInstance(coordinates: Coordinates?): Coordinates? {
            coordinates ?: return null
            return Coordinates(
                longitude = coordinates.longitude?.take(Constants.LAT_LEN_LENGTH),
                latitude = coordinates.latitude?.take(Constants.LAT_LEN_LENGTH)
            )
        }
    }
}