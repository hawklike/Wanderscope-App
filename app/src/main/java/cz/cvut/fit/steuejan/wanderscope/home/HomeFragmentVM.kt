package cz.cvut.fit.steuejan.wanderscope.home

import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.account.repository.AccountRepository
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.event.AnySingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.extension.fireAndForget
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO

class HomeFragmentVM(private val accountRepository: AccountRepository) : BaseViewModel() {

    val logout = AnySingleLiveEvent()

    fun test() {
        viewModelScope.launchIO {
            accountRepository.test().fireAndForget(this)
        }
    }

    fun logout() {
        logout.publish()
    }
}