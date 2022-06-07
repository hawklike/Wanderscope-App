package cz.cvut.fit.steuejan.wanderscope.app.extension

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.LocationManager
import android.view.View
import androidx.annotation.RawRes
import androidx.core.view.doOnLayout
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.ktx.addMarker
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Coordinates
import cz.cvut.fit.steuejan.wanderscope.app.util.runOrNull
import kotlin.math.min

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
        addMarker(it, title, hue)
    }
}

fun GoogleMap.addMarker(
    latLng: LatLng,
    title: String? = null,
    hue: Float = BitmapDescriptorFactory.HUE_RED
): Marker? {
    return addMarker {
        position(latLng)
        title(title)
        icon(BitmapDescriptorFactory.defaultMarker(hue))
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
            val camera = applyPaddingSafe(builder.build(), width, height, relativePadding)
                ?: CameraUpdateFactory.newLatLngZoom(safeMarkers.first().position, zoomLevel)
            this.animateCamera(camera)
        }
    }
}

private fun applyPaddingSafe(
    bounds: LatLngBounds,
    width: Int,
    height: Int,
    relativePadding: Double
): CameraUpdate? {
    val padding = min(width, height) * relativePadding
    return runOrNull {
        CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding.toInt())
    }
}

@SuppressLint("MissingPermission")
fun GoogleMap.zoomToCurrentLocation(context: Context, zoomLevel: Float): Boolean {
    val locationManager = context
        .getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    val criteria = Criteria()
    val location = locationManager?.getBestProvider(criteria, false)
        ?.let { locationManager.getLastKnownLocation(it) }
    location?.let {
        val latLng = LatLng(it.latitude, it.longitude)
        this.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
        return true
    }
    return false
}