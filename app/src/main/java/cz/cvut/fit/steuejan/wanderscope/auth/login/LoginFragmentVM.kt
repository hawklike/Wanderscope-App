package cz.cvut.fit.steuejan.wanderscope.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent.Action
import cz.cvut.fit.steuejan.wanderscope.app.util.multipleLet
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.LoginRequest
import cz.cvut.fit.steuejan.wanderscope.auth.repository.AuthRepository

class LoginFragmentVM(private val authRepository: AuthRepository) : BaseViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun login() {
        viewModelScope.launchIO {
            multipleLet(email.value, password.value) { email, password ->
                val request = LoginRequest(email, password)
                // TODO: failure, loading, success
                authRepository.login(request).safeCollect(this) {
                    when (it) {
                        is Result.Cache -> TODO()
                        is Result.Failure -> {}
                        is Result.Loading -> {}
                        is Result.Success -> navigateTo(
                            Action(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                        )
                    }
                }
            }
        }
    }
}