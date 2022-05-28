package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.bussiness

import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response.TripItineraryResponse

class TripItineraryParser(private val data: TripItineraryResponse) {

    suspend fun prepareItems(): PreparedItems {
        val lastIndex = data.itinerary.lastIndex
        var activeItemIdx = 0
        val items = withDefault {
            data.itinerary.mapIndexed { index, point ->
                if (point.isActive()) {
                    activeItemIdx = index
                }
                point.toItem(index == 0, index == lastIndex)
            }
        }
        return PreparedItems(activeItemIdx, items)
    }

    data class PreparedItems(
        val activeItemIdx: Int,
        val items: List<RecyclerItem>
    )
}