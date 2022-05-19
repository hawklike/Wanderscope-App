package cz.cvut.fit.steuejan.wanderscope.app.extension

import android.content.Context
import androidx.annotation.RawRes
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions

fun GoogleMap.setMapStyle(context: Context, @RawRes mapStyle: Int): Boolean {
    return kotlin.runCatching {
        this.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                context,
                mapStyle
            )
        )
    }.getOrDefault(false)
}