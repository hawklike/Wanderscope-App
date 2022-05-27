package cz.cvut.fit.steuejan.wanderscope.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.account.api.response.AccountResponse
import cz.cvut.fit.steuejan.wanderscope.account.repository.AccountRepository
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.LoadingMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.delayAndReturn
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.livedata.AnySingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.app.util.createAcronym
import cz.cvut.fit.steuejan.wanderscope.app.util.getName

class AccountFragmentVM(
    private val accountRepository: AccountRepository
) : BaseViewModel() {

    val username = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val displayName = MutableLiveData<String?>()
    val acronym = MutableLiveData<String>()

    val logoutEvent = AnySingleLiveEvent()
    val logoutAllLoading = AnySingleLiveEvent()
    val logoutAllSuccess = AnySingleLiveEvent()

    private val accountLoading = MutableLiveData<Boolean>()
    val loading = LoadingMediator(accountLoading).delayAndReturn(Constants.DELAY_LOADING)

    fun logout() {
        showAlertDialog(
            AlertDialogInfo(
                R.string.log_out_dialog_title,
                positiveButton = R.string.log_out,
                onClickPositive = { _, _ -> logoutEvent.publish() })
        )
    }

    fun getAccount() {
        viewModelScope.launchIO {
            accountRepository.getAccount().safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> getAccountFailure(it.error)
                    is Result.Loading -> accountLoading.value = true
                    is Result.Success -> getAccountSuccess(it.data)
                }
            }
        }
    }

    private fun getAccountFailure(error: Error) {
        accountLoading.value = false
        unexpectedError(error)
    }

    private fun getAccountSuccess(data: AccountResponse) {
        username.value = data.username
        email.value = data.email
        displayName.value = data.displayName
        acronym.value = createAcronym(
            getName(data.username, data.displayName)
                .split(" ")
        ) ?: data.username.first().toString()
        accountLoading.value = false
    }

    fun logoutAll() {
        showAlertDialog(
            AlertDialogInfo(
                R.string.log_out_all_dialog_title,
                message = R.string.log_out_all_dialog_message,
                positiveButton = R.string.log_out,
                onClickPositive = { _, _ -> logoutAllDevices() })
        )
    }

    private fun logoutAllDevices() {
        viewModelScope.launchIO {
            accountRepository.logoutAll().safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> logoutFailure(it.error)
                    is Result.Loading -> logoutAllLoading.publish()
                    is Result.Success -> logoutAllSuccess.publish()
                }
            }
        }
    }

    private fun logoutFailure(error: Error) {
        logoutAllLoading.value = false
        unexpectedError(error)
    }
}