package cz.cvut.fit.steuejan.wanderscope.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.LoadingMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.DurationString
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.EmptyItem
import cz.cvut.fit.steuejan.wanderscope.app.extension.delayAndReturn
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.toDurationString
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent.Action
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.app.session.SessionManager
import cz.cvut.fit.steuejan.wanderscope.auth.repository.AuthRepository
import cz.cvut.fit.steuejan.wanderscope.trip.overview.TripOverviewFragment
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response.TripItineraryResponse
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.bussiness.TripItineraryParser
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.repository.ItineraryRepository
import cz.cvut.fit.steuejan.wanderscope.trips.api.response.TripOverviewResponse
import cz.cvut.fit.steuejan.wanderscope.trips.api.response.TripsResponse
import cz.cvut.fit.steuejan.wanderscope.trips.model.TripsScope
import cz.cvut.fit.steuejan.wanderscope.trips.repository.TripsRepository

class HomeFragmentVM(
    private val authRepository: AuthRepository,
    private val tripsRepository: TripsRepository,
    private val itineraryRepository: ItineraryRepository,
    private val sessionManager: SessionManager
) : BaseViewModel() {

    val shouldLogin = liveData {
        emit(!authRepository.isUserLoggedIn())
    }

    val duration = MutableLiveData<DurationString>()
    val title = MutableLiveData<String>()
    val seeMoreVisibility = MutableLiveData<Boolean>()

    private val recommendedTripLoading = MutableLiveData<Boolean>()
    val itineraryLoading = MutableLiveData<Boolean>()

    val itinerary = MutableLiveData<List<RecyclerItem>>()
    val activeItemIdx = MutableLiveData<Int>()

    val tripOverview = MutableLiveData<TripOverviewResponse>()

    val loading = LoadingMediator(
        recommendedTripLoading,
        itineraryLoading
    ).delayAndReturn(Constants.DELAY_LOADING)

    fun getRecommendedTrip(emptyTitle: String) {
        viewModelScope.launchIO {
            if (!sessionManager.isUserLoggedIn()) {
                return@launchIO
            }
            tripsRepository.getTrips(TripsScope.RECOMMENDED).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> failure(it.error, recommendedTripLoading)
                    is Result.Loading -> recommendedTripLoading.value = true
                    is Result.Success -> getRecommendedTripSuccess(it.data, emptyTitle)
                }
            }
        }
    }

    private fun failure(error: Error, loading: MutableLiveData<Boolean>) {
        loading.value = false
        unexpectedError(error)
    }

    private suspend fun getRecommendedTripSuccess(data: TripsResponse, emptyTitle: String) {
        if (data.trips.isEmpty()) {
            seeMoreVisibility.value = false
            duration.value = Duration().toDurationString()
            title.value = emptyTitle
            itinerary.value = listOf()
        } else with(data.trips.first()) {
            tripOverview.value = this
            this@HomeFragmentVM.duration.value = duration.toDurationString()
            title.value = name
            seeMoreVisibility.value = true
            getItinerary(id)
        }
        recommendedTripLoading.value = false
    }

    fun getItinerary(tripId: Int) {
        viewModelScope.launchIO {
            itineraryRepository.getItinerary(tripId).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> failure(it.error, itineraryLoading)
                    is Result.Loading -> itineraryLoading.value = true
                    is Result.Success -> itinerarySuccess(it.data)
                }
            }
        }
    }

    private suspend fun itinerarySuccess(data: TripItineraryResponse) {
        val (activeItemIdx, items) = TripItineraryParser(data).prepareItems()
        this.activeItemIdx.value = activeItemIdx
        itinerary.value = items.ifEmpty { listOf(EmptyItem.itinerary()) }
        itineraryLoading.value = false
    }

    fun seeOverview() {
        val trip = tripOverview.value ?: return
        navigateTo(
            Action(
                HomeFragmentDirections.actionHomeFragmentToTripPagerFragment(
                    trip.id,
                    trip.name,
                    hasBottomNavigation = false,
                    trip.role,
                    TripOverviewFragment.POSITION
                )
            )
        )
    }
}