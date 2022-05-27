package cz.cvut.fit.steuejan.wanderscope.account.change_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.account.api.request.ChangePasswordRequest
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.livedata.LoadingMutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent.Back
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.auth.repository.AuthRepository
import kotlinx.coroutines.delay

class ChangePasswordFragmentVM(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    val oldPassword = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val confirmNewPassword = MutableLiveData<String>()

    val validateOldPassword = oldPassword.switchMapSuspend {
        val validation = validator.validatePassword(it)
        if (validation == OK && newPassword.value == it) {
            R.string.passwords_same_as_new
        } else {
            validation
        }
    }

    val validateNewPassword = newPassword.switchMapSuspend {
        val validation = validator.validatePassword(it)
        if (validation == OK && oldPassword.value == it) {
            R.string.passwords_same_as_current
        } else if (validation == OK && !confirmNewPassword.value.isNullOrBlank()) {
            validator.validateConfirmPassword(confirmNewPassword.value, it).also { error ->
                validateConfirmNewPassword.value = error
            }
        } else {
            validation
        }
    }

    val validateConfirmNewPassword: MutableLiveData<Int> = confirmNewPassword.switchMapSuspend {
        validator.validateConfirmPassword(newPassword.value, it).also { error ->
            if (error == OK && oldPassword.value == it) {
                validateNewPassword.value = R.string.passwords_same_as_current
            } else {
                validateNewPassword.value = error
            }
        }
    }

    val enableSubmit = ValidationMediator(
        validateOldPassword,
        validateNewPassword,
        validateConfirmNewPassword
    )

    val submitLoading = LoadingMutableLiveData()

    fun changePassword() {
        viewModelScope.launchIO {
            val request = ChangePasswordRequest(
                oldPassword.value ?: return@launchIO,
                newPassword.value ?: return@launchIO,
                confirmNewPassword.value ?: return@launchIO
            )
            authRepository.changePassword(request).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> changePasswordFailure(it.error)
                    is Result.Loading -> submitLoading.value = true
                    is Result.Success -> changePasswordSuccess()
                }
            }
        }
    }

    private fun changePasswordSuccess() {
        showToast(ToastInfo(R.string.password_changed))
        navigateTo(Back)
    }

    private suspend fun changePasswordFailure(error: Error) {
        submitLoading.value = false
        delay(50) //ensure that submitLoading is observed first
        when (error.reason?.customCode) {
            1 -> validateOldPassword.value = R.string.entered_password_is_wrong
            else -> unexpectedError(error)
        }
    }
}