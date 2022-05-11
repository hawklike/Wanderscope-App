package cz.cvut.fit.steuejan.wanderscope.trip.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.LoadingMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.DurationString
import cz.cvut.fit.steuejan.wanderscope.app.extension.delayAndReturn
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.toDurationString
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.trip.api.response.TripResponse
import cz.cvut.fit.steuejan.wanderscope.trip.repository.TripRepository
import kotlinx.coroutines.CoroutineScope

class TripOverviewFragmentVM(private val tripRepository: TripRepository) : BaseViewModel() {

    val title = MutableLiveData<String>()
    val duration = MutableLiveData<DurationString>()
    val description = MutableLiveData<String?>()

    val users = MutableLiveData<List<RecyclerItem>>()

    private val tripOverviewLoading = MutableLiveData<Boolean>()

    val loading = LoadingMediator(
        tripOverviewLoading
    ).delayAndReturn()

    fun getTrip(tripId: Int) {
        viewModelScope.launchIO { getTripOverview(tripId, this) }
        viewModelScope.launchIO { getUsers(tripId, this) }
    }

    private suspend fun getTripOverview(tripId: Int, scope: CoroutineScope) {
        tripRepository.getTrip(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> getTripFailure(it.error)
                is Result.Loading -> tripOverviewLoading.value = true
                is Result.Success -> getTripOverviewSuccess(it.data)
            }
        }
    }

    private suspend fun getTripOverviewSuccess(data: TripResponse) {
        title.value = data.name
        duration.value = data.duration.toDurationString()
        description.value = data.description
        tripOverviewLoading.value = false
    }

    private fun getTripFailure(error: Error) {
        tripOverviewLoading.value = false
        unexpectedError(error)
    }

    private suspend fun getUsers(tripId: Int, scope: CoroutineScope) {
        val users = mutableListOf<RecyclerItem>()
        repeat(7) {
            users.add(TripPointOverviewItem(1, "", null, null, 1))
        }
        this.users.postValue(users)
    }

}