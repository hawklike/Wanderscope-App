package cz.cvut.fit.steuejan.wanderscope.app.extension

import android.content.Context
import android.view.View
import androidx.annotation.RawRes
import androidx.core.view.doOnLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ktx.addMarker
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Coordinates

@Suppress("unused")
fun GoogleMap.setCustomStyle(context: Context, @RawRes mapStyle: Int): Boolean {
    return kotlin.runCatching {
        this.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                context,
                mapStyle
            )
        )
    }.getOrDefault(false)
}

/**
 * @param hue default is red, the formula for negative
 * hue is *360 + negativeHue*, otherwise *positiveHue*
 */
fun GoogleMap.addMarker(
    coordinates: Coordinates?,
    title: String? = null,
    hue: Float = BitmapDescriptorFactory.HUE_RED
): Marker? {
    return coordinates?.toLatLng()?.let {
        addMarker {
            position(it)
            title(title)
            icon(BitmapDescriptorFactory.defaultMarker(hue))
        }
    }
}

/**
 * Adjusts zoom so that all markers are displayed on the map.
 * @param view container of the map, the most typically *MapView*
 * @param markers markers to be displayed
 * @param zoomLevel sets the zoom level of the marker when only one marker appears
 * @param relativePadding sets the relative padding of the [view] when multiple markers appear
 */
fun GoogleMap.adjustZoom(
    view: View,
    vararg markers: Marker?,
    zoomLevel: Float = 12F,
    relativePadding: Double = 0.2
) {
    val safeMarkers = markers.filterNotNull()

    if (safeMarkers.size == 1) {
        val latLng = safeMarkers.first().position
        val camera = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel)
        this.animateCamera(camera)
        return
    }
    if (safeMarkers.size > 1) {
        val builder = LatLngBounds.builder()
        safeMarkers.forEach {
            builder.include(it.position)
        }
        view.doOnLayout {
            val width: Int = it.measuredWidth
            val height = it.measuredHeight
            val padding = (width * relativePadding).toInt()
            val camera = CameraUpdateFactory.newLatLngBounds(builder.build(), width, height, padding)
            this.animateCamera(camera)
        }
    }
}