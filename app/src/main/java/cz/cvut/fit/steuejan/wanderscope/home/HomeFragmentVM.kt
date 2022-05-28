package cz.cvut.fit.steuejan.wanderscope.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.LoadingMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.DurationString
import cz.cvut.fit.steuejan.wanderscope.app.extension.delayAndReturn
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.toDurationString
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.auth.repository.AuthRepository
import cz.cvut.fit.steuejan.wanderscope.trips.api.response.TripsResponse
import cz.cvut.fit.steuejan.wanderscope.trips.model.TripsScope
import cz.cvut.fit.steuejan.wanderscope.trips.repository.TripsRepository

class HomeFragmentVM(
    private val authRepository: AuthRepository,
    private val tripsRepository: TripsRepository
) : BaseViewModel() {

    val shouldLogin = liveData {
        emit(!authRepository.isUserLoggedIn())
    }

    val duration = MutableLiveData<DurationString>()
    val title = MutableLiveData<String>()
    val seeMoreVisibility = MutableLiveData<Boolean>()

    private val recommendedTripLoading = MutableLiveData<Boolean>()

    val loading = LoadingMediator(
        recommendedTripLoading
    ).delayAndReturn(Constants.DELAY_LOADING)

    fun getRecommendedTrip(emptyTitle: String, init: Boolean) {
        viewModelScope.launchIO {
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
        } else with(data.trips.first()) {
            this@HomeFragmentVM.duration.value = duration.toDurationString()
            title.value = name
            seeMoreVisibility.value = true
        }
        recommendedTripLoading.value = false
    }
}