package cz.cvut.fit.steuejan.wanderscope.auth.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault

class RegisterFragmentVM : BaseViewModel() {

    val username = MutableLiveData<String>()

    val validateUsername = username.switchMapSuspend {
        validateUsername(it)
    }

    private suspend fun validateUsername(username: String) = withDefault {
        when {
            username.isBlank() -> R.string.validation_empty
            username.length < 8 -> R.string.validation_username_short
            else -> null
        }
    }

    fun register() {
        viewModelScope.launchIO {
            //todo
        }
    }
}