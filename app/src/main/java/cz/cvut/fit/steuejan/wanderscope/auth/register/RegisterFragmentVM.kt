package cz.cvut.fit.steuejan.wanderscope.auth.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend

class RegisterFragmentVM(private val validator: InputValidator) : BaseViewModel() {

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
        validator.validatePassword(it)
    } as MutableLiveData<Int>

    val validateConfirmPassword = confirmPassword.switchMapSuspend {
        validator.validateConfirmPassword(password.value, it).also { error ->
            validatePassword.value = error
        }
    }

    val enabled = ValidationMediator(
        validateUsername,
        validateEmail,
        validatePassword,
        validateConfirmPassword
    )

    fun register() {
        viewModelScope.launchIO {
            //todo
        }
    }
}