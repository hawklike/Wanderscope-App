package cz.cvut.fit.steuejan.wanderscope.trip.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.LoadingMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.DurationString
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.EmptyItem
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.toDurationString
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.response.MultipleAccommodationResponse
import cz.cvut.fit.steuejan.wanderscope.points.activity.response.ActivitiesResponse
import cz.cvut.fit.steuejan.wanderscope.points.place.response.PlacesResponse
import cz.cvut.fit.steuejan.wanderscope.points.transport.response.TransportsResponse
import cz.cvut.fit.steuejan.wanderscope.trip.api.response.TripResponse
import cz.cvut.fit.steuejan.wanderscope.trip.repository.TripRepository
import kotlinx.coroutines.CoroutineScope

class TripOverviewFragmentVM(private val tripRepository: TripRepository) : BaseViewModel() {

    val title = MutableLiveData<String>()
    val duration = MutableLiveData<DurationString>()
    val description = MutableLiveData<String?>()

    val transport = MutableLiveData<List<RecyclerItem>>()
    val accommodation = MutableLiveData<List<RecyclerItem>>()
    val places = MutableLiveData<List<RecyclerItem>>()
    val activities = MutableLiveData<List<RecyclerItem>>()

    private val tripOverviewLoading = MutableLiveData<Boolean>()
    private val accommodationLoading = MutableLiveData<Boolean>()
    private val transportLoading = MutableLiveData<Boolean>()
    private val placesLoading = MutableLiveData<Boolean>()
    private val activitiesLoading = MutableLiveData<Boolean>()

    val loading = LoadingMediator(
        tripOverviewLoading,
        accommodationLoading,
        transportLoading,
        placesLoading,
        activitiesLoading
    )

    fun getTrip(tripId: Int) {
        viewModelScope.launchIO { getTripOverview(tripId, this) }
        viewModelScope.launchIO { getAccommodation(tripId, this) }
        viewModelScope.launchIO { getTransport(tripId, this) }
        viewModelScope.launchIO { getActivities(tripId, this) }
        viewModelScope.launchIO { getPlaces(tripId, this) }
    }

    private suspend fun getTripOverview(tripId: Int, scope: CoroutineScope) {
        tripRepository.getTrip(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, tripOverviewLoading)
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

    private fun failure(error: Error, loading: MutableLiveData<Boolean>) {
        loading.value = false
        unexpectedError(error)
    }

    private suspend fun getAccommodation(tripId: Int, scope: CoroutineScope) {
        tripRepository.getAccommodation(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, accommodationLoading)
                is Result.Loading -> accommodationLoading.value = true
                is Result.Success -> accommodationSuccess(it.data)
            }
        }
    }

    private suspend fun accommodationSuccess(data: MultipleAccommodationResponse) {
        val items = data.accommodation.map { it.toOverviewItem() }
        accommodation.value = items.ifEmpty { listOf(EmptyItem.accommodation()) }
        accommodationLoading.value = false
    }

    private suspend fun getTransport(tripId: Int, scope: CoroutineScope) {
        tripRepository.getTransports(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, transportLoading)
                is Result.Loading -> transportLoading.value = true
                is Result.Success -> transportSuccess(it.data)
            }
        }
    }

    private suspend fun transportSuccess(data: TransportsResponse) {
        val items = data.transports.map { it.toOverviewItem() }
        transport.value = items.ifEmpty { listOf(EmptyItem.transport()) }
        transportLoading.value = false
    }

    private suspend fun getActivities(tripId: Int, scope: CoroutineScope) {
        tripRepository.getActivities(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, activitiesLoading)
                is Result.Loading -> activitiesLoading.value = true
                is Result.Success -> activitiesSuccess(it.data)
            }
        }
    }

    private suspend fun activitiesSuccess(data: ActivitiesResponse) {
        val items = data.activities.map { it.toOverviewItem() }
        activities.value = items.ifEmpty { listOf(EmptyItem.activities()) }
        activitiesLoading.value = false
    }

    private suspend fun getPlaces(tripId: Int, scope: CoroutineScope) {
        tripRepository.getPlaces(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, placesLoading)
                is Result.Loading -> placesLoading.value = true
                is Result.Success -> placesSuccess(it.data)
            }
        }
    }

    private suspend fun placesSuccess(data: PlacesResponse) {
        val items = data.places.map { it.toOverviewItem() }
        places.value = items.ifEmpty { listOf(EmptyItem.places()) }
        placesLoading.value = false
    }
}