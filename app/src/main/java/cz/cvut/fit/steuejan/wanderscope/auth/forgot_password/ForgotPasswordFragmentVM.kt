package cz.cvut.fit.steuejan.wanderscope.auth.forgot_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.livedata.LoadingMutableLiveData
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.ForgotPasswordRequest
import cz.cvut.fit.steuejan.wanderscope.auth.repository.AuthRepository

class ForgotPasswordFragmentVM(private val authRepository: AuthRepository) : BaseViewModel() {

    val email = MutableLiveData<String>()

    val validateEmail = email.switchMapSuspend {
        validator.validateEmail(it)
    }

    val enableSubmit = ValidationMediator(validateEmail)

    val forgotPasswordLoading = LoadingMutableLiveData()

    fun forgotPassword() {
        viewModelScope.launchIO {
            val request = ForgotPasswordRequest(email.value ?: return@launchIO)
            validateEmail.postValue(null)
            authRepository.forgotPassword(request).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> forgotPasswordFailure()
                    is Result.Loading -> forgotPasswordLoading.value = true
                    is Result.Success -> forgotPasswordSuccess()
                }
            }
        }
    }

    private fun forgotPasswordSuccess() {
        forgotPasswordLoading.value = false
        showSnackbar(BaseViewModel.SnackbarInfo(R.string.forgot_password_email))
    }

    private fun forgotPasswordFailure() {
        forgotPasswordLoading.value = false
        validateEmail.value = R.string.unexpected_error
    }
}