package cz.cvut.fit.steuejan.wanderscope.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.livedata.LoadingMutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent.Action
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.LoginRequest
import cz.cvut.fit.steuejan.wanderscope.auth.repository.AuthRepository

class LoginFragmentVM(private val authRepository: AuthRepository) : BaseViewModel() {

    private var loginFailed = false

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val validateEmail = email.switchMapSuspend {
        restoreErrorsAfterFailedLogin()
        validator.validateEmail(it)
    }

    val validatePassword = password.switchMapSuspend {
        restoreErrorsAfterFailedLogin()
        validator.validatePassword(it)
    }

    private val allValidations = arrayOf(
        validateEmail,
        validatePassword
    )

    val enableLogin = ValidationMediator(*allValidations)

    val loginLoading = LoadingMutableLiveData()

    fun login() {
        clearAllValidations()
        viewModelScope.launchIO {
            val request = LoginRequest(email.value ?: return@launchIO, password.value ?: return@launchIO)
            authRepository.login(request).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> invalidLogin()
                    is Result.Loading -> loginLoading.value = true
                    is Result.Success -> goToHomeScreen()
                }
            }
        }
    }

    private fun invalidLogin() {
        loginLoading.value = false
        loginFailed = true
        validateEmail.value = R.string.login_failed
        validatePassword.value = R.string.login_failed
    }

    private fun restoreErrorsAfterFailedLogin() {
        if (loginFailed) {
            loginFailed = false
            validateEmail.value = OK
            validatePassword.value = OK
        }
    }

    private fun goToHomeScreen() {
        navigateTo(Action(LoginFragmentDirections.actionLoginFragmentToHomeFragment()))
    }

    fun register() {
        navigateTo(Action(LoginFragmentDirections.actionLoginFragmentToRegisterFragment()))
    }

    fun forgotPassword() {
        navigateTo(Action(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()))
    }

    private fun clearAllValidations() {
        allValidations.forEach {
            it.value = OK
        }
    }
}