package cz.cvut.fit.steuejan.wanderscope

import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.auth.repository.AuthRepository
import kotlinx.coroutines.launch

class MainActivityVM(private val authRepository: AuthRepository) : BaseViewModel() {

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}