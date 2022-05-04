package cz.cvut.fit.steuejan.wanderscope.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent.Action
import cz.cvut.fit.steuejan.wanderscope.app.util.multipleLet
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.ForgotPasswordRequest
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.LoginRequest
import cz.cvut.fit.steuejan.wanderscope.auth.repository.AuthRepository

class LoginFragmentVM(private val authRepository: AuthRepository) : BaseViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun login() {
        viewModelScope.launchIO {
            //TODO validate email, password
            multipleLet(email.value, password.value) { email, password ->
                val request = LoginRequest(email, password)
                authRepository.login(request).safeCollect(this) {
                    when (it) {
                        is Result.Cache -> TODO()
                        is Result.Failure -> {} //todo
                        is Result.Loading -> {} //todo
                        is Result.Success -> goToHomeScreen()
                    }
                }
            }
        }
    }

    fun register() {
        navigateTo(Action(LoginFragmentDirections.actionLoginFragmentToRegisterFragment()))
    }

    fun forgotPassword() {
        viewModelScope.launchIO {
            email.value?.let { email ->
                val request = ForgotPasswordRequest(email)
                authRepository.forgotPassword(request).safeCollect(this) {
                    when (it) {
                        is Result.Cache -> TODO()
                        is Result.Failure -> TODO()
                        is Result.Loading -> {} //todo
                        is Result.Success -> showSnackbar(SnackbarInfo(R.string.forgot_password_email))
                    }
                }
            }
        }
    }

    private fun goToHomeScreen() {
        navigateTo(Action(LoginFragmentDirections.actionLoginFragmentToHomeFragment()))
    }
}