package cz.cvut.fit.steuejan.wanderscope.trip.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.R
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
import cz.cvut.fit.steuejan.wanderscope.document.response.DocumentsMetadataResponse
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.response.MultipleAccommodationResponse
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.response.ActivitiesResponse
import cz.cvut.fit.steuejan.wanderscope.points.place.api.response.PlacesResponse
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.response.TransportsResponse
import cz.cvut.fit.steuejan.wanderscope.trip.api.response.TripResponse
import cz.cvut.fit.steuejan.wanderscope.trip.repository.TripRepository
import cz.cvut.fit.steuejan.wanderscope.user.api.response.UsersResponse
import kotlinx.coroutines.CoroutineScope

class TripOverviewFragmentVM(
    private val tripRepository: TripRepository
) : BaseViewModel() {

    val title = MutableLiveData<String>()
    val duration = MutableLiveData<DurationString>()
    val description = MutableLiveData<String?>()

    val transport = MutableLiveData<List<RecyclerItem>>()
    val accommodation = MutableLiveData<List<RecyclerItem>>()
    val places = MutableLiveData<List<RecyclerItem>>()
    val activities = MutableLiveData<List<RecyclerItem>>()
    val documents = MutableLiveData<List<RecyclerItem>>()
    val travellers = MutableLiveData<List<RecyclerItem>>()

    val tripOverview = MutableLiveData<TripResponse>()

    private val tripOverviewLoading = MutableLiveData<Boolean>()
    private val accommodationLoading = MutableLiveData<Boolean>()
    private val transportLoading = MutableLiveData<Boolean>()
    private val placesLoading = MutableLiveData<Boolean>()
    private val activitiesLoading = MutableLiveData<Boolean>()
    private val documentsLoading = MutableLiveData<Boolean>()
    private val travellersLoading = MutableLiveData<Boolean>()

    val loading = LoadingMediator(
        tripOverviewLoading,
        accommodationLoading,
        transportLoading,
        placesLoading,
        activitiesLoading,
        documentsLoading,
        travellersLoading
    )

    fun getTrip(tripId: Int) {
        viewModelScope.launchIO { getTripOverview(tripId, this) }
        viewModelScope.launchIO { getAccommodation(tripId, this) }
        viewModelScope.launchIO { getTransport(tripId, this) }
        viewModelScope.launchIO { getActivities(tripId, this) }
        viewModelScope.launchIO { getPlaces(tripId, this) }
        viewModelScope.launchIO { getDocuments(tripId, this) }
        viewModelScope.launchIO { getUsers(tripId, this) }
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
        tripOverview.value = data
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
        showUpdateToast(items, accommodation.value, R.string.accommodation_updated)
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
        showUpdateToast(items, transport.value, R.string.transport_updated)
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
        showUpdateToast(items, activities.value, R.string.activities_updated)
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
        showUpdateToast(items, places.value, R.string.places_updated)
        places.value = items.ifEmpty { listOf(EmptyItem.places()) }
        placesLoading.value = false
    }

    private suspend fun getDocuments(tripId: Int, scope: CoroutineScope) {
        tripRepository.getDocuments(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, documentsLoading)
                is Result.Loading -> documentsLoading.value = true
                is Result.Success -> documentsSuccess(it.data)
            }
        }
    }

    private suspend fun documentsSuccess(data: DocumentsMetadataResponse) {
        val items = data.documents.map { it.toOverviewItem() }
        showUpdateToast(items, documents.value, R.string.documents_updated)
        documents.value = items.ifEmpty { listOf(EmptyItem.documents()) }
        documentsLoading.value = false
    }

    private suspend fun getUsers(tripId: Int, scope: CoroutineScope) {
        tripRepository.getUsers(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, travellersLoading)
                is Result.Loading -> travellersLoading.value = true
                is Result.Success -> usersSuccess(it.data)
            }
        }
    }

    private fun usersSuccess(data: UsersResponse) {
        val items = data.users.map { it.toItem(false) }
        travellers.value = items
        showUpdateToast(items, travellers.value, R.string.travellers_updated)
        travellersLoading.value = false
    }
}