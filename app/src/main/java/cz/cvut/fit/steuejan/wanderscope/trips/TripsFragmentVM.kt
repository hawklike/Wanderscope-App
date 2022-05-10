package cz.cvut.fit.steuejan.wanderscope.trips

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.EmptyItem
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent.Action
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.trips.api.response.TripsResponse
import cz.cvut.fit.steuejan.wanderscope.trips.model.TripsScope
import cz.cvut.fit.steuejan.wanderscope.trips.repository.TripsRepository
import kotlinx.coroutines.CoroutineScope

class TripsFragmentVM(private val tripsRepository: TripsRepository) : BaseViewModel() {

    val upcomingTrips = MutableLiveData<List<RecyclerItem>>()
    val pastTrips = MutableLiveData<List<RecyclerItem>>()

    fun getTrips() {
        showLoading()
        viewModelScope.launchIO { getUpcomingTrips(this) }
        viewModelScope.launchIO { getPastTrips(this) }
    }

    fun addTrip() {
        navigateTo(Action(TripsFragmentDirections.actionTripsFragmentToAddTripFragment()))
    }

    private suspend fun getUpcomingTrips(scope: CoroutineScope) {
        tripsRepository.getTrips(TripsScope.UPCOMING).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> unexpectedError(it.error)
                is Result.Loading -> doNothing
                is Result.Success -> upcomingTripsSuccess(it.data)
            }
        }
    }

    private suspend fun upcomingTripsSuccess(data: TripsResponse) {
        upcomingTrips.value = tripsSuccess(data, EmptyItem.upcomingTrips())
        hideLoading()
    }

    private suspend fun tripsSuccess(data: TripsResponse, emptyItem: EmptyItem): List<RecyclerItem> {
        return if (data.trips.isEmpty()) {
            listOf(emptyItem)
        } else {
            data.trips.map { it.toItem() }
        }
    }

    private suspend fun getPastTrips(scope: CoroutineScope) {
        tripsRepository.getTrips(TripsScope.PAST).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> unexpectedError(it.error)
                is Result.Loading -> doNothing
                is Result.Success -> pastTripsSuccess(it.data)
            }
        }
    }

    private suspend fun pastTripsSuccess(data: TripsResponse) {
        pastTrips.value = tripsSuccess(data, EmptyItem.pastTrips())
    }
}