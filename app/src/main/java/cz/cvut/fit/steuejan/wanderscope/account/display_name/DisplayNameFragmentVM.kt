package cz.cvut.fit.steuejan.wanderscope.account.display_name

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.account.api.request.ChangeDisplayNameRequest
import cz.cvut.fit.steuejan.wanderscope.account.repository.AccountRepository
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.livedata.AnySingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.livedata.LoadingMutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error

class DisplayNameFragmentVM(
    private val accountRepository: AccountRepository
) : BaseViewModel() {

    val title = MutableLiveData<Int>()
    val displayName = MutableLiveData<String>()

    val validateDisplayName = displayName.switchMapSuspend {
        validator.validateDisplayName(it)
    }

    val enableSubmit = ValidationMediator(validateDisplayName)
    val loading = LoadingMutableLiveData()

    val requestIsSuccess = AnySingleLiveEvent()

    fun init(displayName: String?) {
        if (displayName != null) {
            this.displayName.value = displayName
            title.value = R.string.change_display_name
        } else {
            title.value = R.string.set_display_name
        }
    }

    fun setDisplayName() {
        viewModelScope.launchIO {
            val displayNameValue = displayName.value
            val displayName = if (displayNameValue.isNullOrBlank()) null else displayNameValue
            val request = ChangeDisplayNameRequest(displayName)
            accountRepository.changeDisplayName(request).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> displayNameFailure(it.error)
                    is Result.Loading -> loading.value = true
                    is Result.Success -> requestIsSuccess.publish()
                }
            }
        }
    }

    private fun displayNameFailure(error: Error) {
        loading.value = false
        unexpectedError(error)
    }
}