package cz.cvut.fit.steuejan.wanderscope

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.account.api.AccountApi
import cz.cvut.fit.steuejan.wanderscope.account.api.DateRequest
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall

class SecondFragmentVM(private val api: AccountApi) : BaseViewModel() {

    val text = MutableLiveData<String>()

    fun sendRequest() {
        val date = DateRequest(2022, 4, 5, 23, 2)

        viewModelScope.launchIO {
            performCall { api.postDate(date) }.safeCollect(this) {
                text.value = when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> it.error.status.name
                    is Result.Loading -> "loading..."
                    is Result.Success -> it.data.time
                }
            }
        }
    }
}