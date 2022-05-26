package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.LoadingMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.EmptyItem
import cz.cvut.fit.steuejan.wanderscope.app.extension.delayAndReturn
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response.TripItineraryResponse
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.repository.ItineraryRepository

class TripItineraryFragmentVM(
    private val itineraryRepository: ItineraryRepository
) : BaseViewModel() {

    val itinerary = MutableLiveData<List<RecyclerItem>>()
    val activeItemIdx = MutableLiveData<Int>()

    private val itineraryLoading = MutableLiveData<Boolean>()

    val loading = LoadingMediator(itineraryLoading).delayAndReturn(Constants.DELAY_LOADING)

    fun showItinerary(tripId: Int) {
        viewModelScope.launchIO {
            itineraryRepository.getItinerary(tripId).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> itineraryFailure(it.error)
                    is Result.Loading -> itineraryLoading.value = true
                    is Result.Success -> itinerarySuccess(it.data)
                }
            }
        }
    }

    private suspend fun itinerarySuccess(data: TripItineraryResponse) {
        val items = prepareItems(data)
        itinerary.value = items.ifEmpty { listOf(EmptyItem.itinerary()) }
        itineraryLoading.value = false
    }

    private suspend fun prepareItems(data: TripItineraryResponse): List<RecyclerItem> {
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
        this.activeItemIdx.value = activeItemIdx
        return items
    }

    private fun itineraryFailure(error: Error) {
        itineraryLoading.value = false
        unexpectedError(error)
    }
}