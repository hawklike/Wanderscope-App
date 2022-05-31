package cz.cvut.fit.steuejan.wanderscope.app.common.map

import com.google.android.gms.maps.model.LatLng

data class LatLngBundle(
    val latLng: LatLng,
    val title: String
)
