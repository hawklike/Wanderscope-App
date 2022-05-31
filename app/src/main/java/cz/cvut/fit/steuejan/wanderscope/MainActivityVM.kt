package cz.cvut.fit.steuejan.wanderscope

import androidx.lifecycle.*
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.SharedViewModel
import cz.cvut.fit.steuejan.wanderscope.app.extension.fireAndForget
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.withIO
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.auth.repository.AuthRepository
import kotlinx.coroutines.launch

class MainActivityVM(
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(savedStateHandle), SharedViewModel {

    val showSplashScreen = MutableLiveData(true)

    override val sharedData = MutableLiveData<Any?>()

    val updateTrip = SingleLiveEvent<Boolean>()
    val updateTripPoint = SingleLiveEvent<Boolean>()
    val updateDocument = SingleLiveEvent<Boolean>()

    val splashScreenAfterProcessDeath = liveData {
        val splashScreenState = withIO {
            getStateData<Boolean>(SPLASHSCREEN_STATE)
        }
        emit(splashScreenState)
    }

    fun hideSplashScreen() {
        showSplashScreen.value = false
        viewModelScope.launch {
            setStateData(SPLASHSCREEN_STATE, false)
        }
    }

    fun logout() {
        viewModelScope.launchIO {
            authRepository.logout().fireAndForget(this)
        }
    }

    fun shouldLogoutUser() = authRepository.shouldLogoutUser().asLiveData()

    companion object {
        const val SPLASHSCREEN_STATE = "splashScreenState"
    }
}