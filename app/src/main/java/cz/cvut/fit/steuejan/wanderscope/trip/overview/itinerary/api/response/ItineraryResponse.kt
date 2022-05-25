package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.serialization.MoshiSerializer
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.model.ItineraryType

sealed class ItineraryResponse(
    @Json(name = MoshiSerializer.ITINERARY_LABEL)
    val type: ItineraryType
) {
    abstract suspend fun toItem(first: Boolean, last: Boolean): RecyclerItem
    abstract suspend fun isActive(): Boolean
}