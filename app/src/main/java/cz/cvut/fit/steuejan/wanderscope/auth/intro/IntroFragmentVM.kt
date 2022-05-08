package cz.cvut.fit.steuejan.wanderscope.auth.intro

import androidx.lifecycle.liveData
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.auth.repository.AuthRepository

class IntroFragmentVM(private val authRepository: AuthRepository) : BaseViewModel() {

    val shouldLogin = liveData {
        emit(!authRepository.isUserLoggedIn())
    }
}