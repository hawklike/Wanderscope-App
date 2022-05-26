package cz.cvut.fit.steuejan.wanderscope.trip.users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.LoadingMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.extension.delayAndReturn
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.trip.repository.TripRepository
import cz.cvut.fit.steuejan.wanderscope.trip.users.repository.TripUserRepository
import cz.cvut.fit.steuejan.wanderscope.user.api.response.UsersResponse

class TripUsersFragmentVM(
    private val tripRepository: TripRepository,
    private val tripUserRepository: TripUserRepository
) : BaseViewModel() {

    val users = MutableLiveData<List<RecyclerItem>>()

    val usersLoading = MutableLiveData<Boolean>()
    val laoding = LoadingMediator(usersLoading).delayAndReturn(Constants.DELAY_LOADING)

    fun loadUsers(tripId: Int, userRole: UserRole) {
        viewModelScope.launchIO {
            tripRepository.getUsers(tripId).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> unexpectedError(it.error)
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

}