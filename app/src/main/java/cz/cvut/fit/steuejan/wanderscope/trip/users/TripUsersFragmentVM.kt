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
import cz.cvut.fit.steuejan.wanderscope.app.extension.*
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.app.session.SessionManager
import cz.cvut.fit.steuejan.wanderscope.trip.repository.TripRepository
import cz.cvut.fit.steuejan.wanderscope.user.api.response.UserResponse
import cz.cvut.fit.steuejan.wanderscope.user.api.response.UsersResponse

class TripUsersFragmentVM(
    private val tripRepository: TripRepository,
    private val sessionManager: SessionManager
) : BaseViewModel() {

    val users = MutableLiveData<List<RecyclerItem>>()

    val addUserEvent = SingleLiveEvent<Purpose>()

    val userRole = MutableLiveData<UserRole?>()

    private val usersLoading = MutableLiveData<Boolean>()
    val loading = LoadingMediator(usersLoading).delayAndReturn(Constants.DELAY_LOADING)

    fun loadUsers(tripId: Int) {
        viewModelScope.launchIO {
            tripRepository.getUsers(tripId).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> loadUserFailure(it.error)
                    is Result.Loading -> usersLoading.value = true
                    is Result.Success -> loadUsersSuccess(it.data)
                }
            }
        }
    }

    private suspend fun loadUsersSuccess(data: UsersResponse) {
        val userId = withIO { sessionManager.getUserId() }
        val loggedUser = findUser(userId, data)
        userRole.value = loggedUser?.role
        val menu = loggedUser?.role == UserRole.ADMIN
        val items = withDefault { data.users.map { it.toItem(menu) } }
        users.value = items
        usersLoading.value = false
    }

    private suspend fun findUser(userId: Int?, response: UsersResponse): UserResponse? {
        return withDefault {
            response.users.find {
                it.id == userId
            }
        }
    }

    private fun loadUserFailure(error: Error) {
        usersLoading.value = false
        unexpectedError(error)
    }

    fun addUser() {
        addUserEvent.value = Purpose.ADD
    }

}