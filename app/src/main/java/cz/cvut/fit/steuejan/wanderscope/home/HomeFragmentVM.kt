package cz.cvut.fit.steuejan.wanderscope.home

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.account.repository.AccountRepository
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.extension.fireAndForget
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.livedata.AnySingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.auth.repository.AuthRepository

class HomeFragmentVM(
    private val authRepository: AuthRepository,
    private val accountRepository: AccountRepository
) : BaseViewModel() {

    val logout = AnySingleLiveEvent()

    fun test() {
        viewModelScope.launchIO {
            accountRepository.test().fireAndForget(this)
        }
    }

    val shouldLogin = liveData {
        emit(!authRepository.isUserLoggedIn())
    }

    fun logout() {
        logout.publish()
    }
}