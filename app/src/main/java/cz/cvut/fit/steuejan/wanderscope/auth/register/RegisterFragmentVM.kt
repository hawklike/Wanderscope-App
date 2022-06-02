package cz.cvut.fit.steuejan.wanderscope.auth.register

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
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.RegisterRequest
import cz.cvut.fit.steuejan.wanderscope.auth.repository.AuthRepository
import kotlinx.coroutines.delay

class RegisterFragmentVM(private val authRepository: AuthRepository) : BaseViewModel() {

    val username = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    val validateUsername = username.switchMapSuspend {
        validator.validateUsername(it)
    }

    val validateEmail = email.switchMapSuspend {
        validator.validateEmail(it)
    }

    val validatePassword = password.switchMapSuspend {
        val validation = validator.validatePassword(it)
        if (validation == OK && !confirmPassword.value.isNullOrBlank()) {
            validator.validateConfirmPassword(confirmPassword.value, it).also { error ->
                validateConfirmPassword.value = error
            }
        } else {
            validation
        }
    }

    val validateConfirmPassword: MutableLiveData<Int> = confirmPassword.switchMapSuspend {
        validator.validateConfirmPassword(password.value, it).also { error ->
            validatePassword.value = error
        }
    }

    val enableSignUp = ValidationMediator(
        validateUsername,
        validateEmail,
        validatePassword,
        validateConfirmPassword
    )

    val signupLoading = LoadingMutableLiveData()

    fun register() {
        viewModelScope.launchIO {
            val request = RegisterRequest(
                username.value ?: return@launchIO,
                email.value ?: return@launchIO,
                password.value ?: return@launchIO,
                confirmPassword.value ?: return@launchIO
            )
            authRepository.register(request).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> handleFailure(it.error)
                    is Result.Loading -> signupLoading.value = true
                    is Result.Success -> goToHomeScreen()
                }
            }
        }
    }

    private fun goToHomeScreen() {
        navigateTo(Action(RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()))
    }

    private suspend fun handleFailure(error: Error) {
        signupLoading.value = false
        delay(50) //ensure that signupLoading is observed first
        when (error.reason?.customCode) {
            1 -> validateEmail.value = R.string.register_email_already_exists
            2 -> validateUsername.value = R.string.register_username_already_exists
            else -> unexpectedError()
        }
    }
}