package cz.cvut.fit.steuejan.wanderscope.trips

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.LoadingMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.EmptyItem
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent.Action
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.trips.api.response.TripsResponse
import cz.cvut.fit.steuejan.wanderscope.trips.model.TripsScope
import cz.cvut.fit.steuejan.wanderscope.trips.repository.TripsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

class TripsFragmentVM(private val tripsRepository: TripsRepository) : BaseViewModel() {

    val upcomingTrips = MutableLiveData<List<RecyclerItem>>()
    val pastTrips = MutableLiveData<List<RecyclerItem>>()

    private val upcomingTripsLoading = MutableLiveData<Boolean>()
    private val pastTripsLoading = MutableLiveData<Boolean>()

    val loading = LoadingMediator(
        upcomingTripsLoading,
        pastTripsLoading
    ).switchMapSuspend {
        delay(200)
        it
    }

    fun getTrips() {
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
                is Result.Failure -> upcomingTripsFailure(it.error)
                is Result.Loading -> upcomingTripsLoading.value = true
                is Result.Success -> upcomingTripsSuccess(it.data)
            }
        }
    }

    private suspend fun upcomingTripsSuccess(data: TripsResponse) {
        upcomingTripsLoading.value = false
        upcomingTrips.value = tripsSuccess(data, EmptyItem.upcomingTrips())
        hideLoading()
    }

    private fun upcomingTripsFailure(error: Error) {
        upcomingTripsLoading.value = false
        unexpectedError(error)
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
                is Result.Failure -> pastTripsFailure(it.error)
                is Result.Loading -> pastTripsLoading.value = true
                is Result.Success -> pastTripsSuccess(it.data)
            }
        }
    }

    private fun pastTripsFailure(error: Error) {
        pastTripsLoading.value = false
        unexpectedError(error)
    }

    private suspend fun pastTripsSuccess(data: TripsResponse) {
        pastTripsLoading.value = false
        pastTrips.value = tripsSuccess(data, EmptyItem.pastTrips())
    }
}