package cz.cvut.fit.steuejan.wanderscope.trip.users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.LoadingMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.common.Purpose
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.extension.delayAndReturn
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.trip.repository.TripRepository
import cz.cvut.fit.steuejan.wanderscope.user.api.response.UsersResponse

class TripUsersFragmentVM(
    private val tripRepository: TripRepository
) : BaseViewModel() {

    val users = MutableLiveData<List<RecyclerItem>>()

    val addUserEvent = SingleLiveEvent<Purpose>()

    private val usersLoading = MutableLiveData<Boolean>()
    val loading = LoadingMediator(usersLoading).delayAndReturn(Constants.DELAY_LOADING)

    fun loadUsers(tripId: Int, userRole: UserRole) {
        viewModelScope.launchIO {
            tripRepository.getUsers(tripId).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> loadUserFailure(it.error)
                    is Result.Loading -> usersLoading.value = true
                    is Result.Success -> loadUsersSuccess(it.data, userRole)
                }
            }
        }
    }

    private suspend fun loadUsersSuccess(data: UsersResponse, userRole: UserRole) {
        val items = withDefault {
            data.users.map {
                it.toItem(userRole == UserRole.ADMIN)
            }
        }
        users.value = items
        usersLoading.value = false
    }

    private fun loadUserFailure(error: Error) {
        usersLoading.value = false
        unexpectedError(error)
    }

    fun addUser() {
        addUserEvent.value = Purpose.ADD
    }

}