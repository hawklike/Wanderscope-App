package cz.cvut.fit.steuejan.wanderscope.trip.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.R
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

    val transport = MutableLiveData<List<RecyclerItem>>()
    val accommodation = MutableLiveData<List<RecyclerItem>>()
    val places = MutableLiveData<List<RecyclerItem>>()
    val activities = MutableLiveData<List<RecyclerItem>>()

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
        val transport = mutableListOf<RecyclerItem>(
            TripPointOverviewItem(1, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_light_aircraft),
            TripPointOverviewItem(2, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_bike),
            TripPointOverviewItem(3, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_car),
            TripPointOverviewItem(4, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_bus),
            TripPointOverviewItem(5, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_train),
            TripPointOverviewItem(6, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_boat),
            TripPointOverviewItem(7, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_public_transport),
            TripPointOverviewItem(8, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_walking),
        )

        val accommodation = mutableListOf<RecyclerItem>(
            TripPointOverviewItem(1, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_hotel),
            TripPointOverviewItem(2, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_hostel),
            TripPointOverviewItem(3, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_pension),
            TripPointOverviewItem(4, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_camp),
            TripPointOverviewItem(5, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_sleeping_bag),
            TripPointOverviewItem(6, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_airbnb),
        )

        val places = mutableListOf<RecyclerItem>(
            TripPointOverviewItem(1, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_outdoor),
            TripPointOverviewItem(2, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_place),
            TripPointOverviewItem(3, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_parking),
            TripPointOverviewItem(4, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_food),
        )

        val activities = mutableListOf<RecyclerItem>(
            TripPointOverviewItem(1, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_hiking),
            TripPointOverviewItem(2, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_bike),
            TripPointOverviewItem(3, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_kayak),
            TripPointOverviewItem(4, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_skiing),
            TripPointOverviewItem(5, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_swimming),
            TripPointOverviewItem(6, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_running),
            TripPointOverviewItem(7, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_climbing),
            TripPointOverviewItem(8, "Ostrava-Praha", "12.12.2022", "18.12.2022", R.drawable.ic_trophy),
        )

        this.transport.postValue(transport)
        this.accommodation.postValue(accommodation)
        this.places.postValue(places)
        this.activities.postValue(activities)
    }

}