package cz.cvut.fit.steuejan.wanderscope.app.common.map

import com.google.android.gms.maps.model.LatLng
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType

data class LatLngBundle(
    val latLng: LatLng,
    val title: String,
    val pointType: TripPointType
)
