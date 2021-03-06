package cz.cvut.fit.steuejan.wanderscope.app.common.map

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView

interface WithMap {

    val map: MapView

    fun onMapReady(googleMap: GoogleMap)
}