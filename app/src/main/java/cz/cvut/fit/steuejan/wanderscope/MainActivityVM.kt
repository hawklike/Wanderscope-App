package cz.cvut.fit.steuejan.wanderscope

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.extension.fireAndForget
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.auth.repository.AuthRepository

class MainActivityVM(private val authRepository: AuthRepository) : BaseViewModel() {

    val showSplashScreen = MutableLiveData(true)

    fun hideSplashScreen() {
        showSplashScreen.value = false
    }

    fun logout() {
        viewModelScope.launchIO {
            authRepository.logout().fireAndForget(this)
        }
    }

    fun shouldLogoutUser() = authRepository.shouldLogoutUser().asLiveData()
}