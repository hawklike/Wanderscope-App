package cz.cvut.fit.steuejan.wanderscope.trip.users.crud

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.common.Purpose
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.livedata.AnySingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.livedata.LoadingMutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Status
import cz.cvut.fit.steuejan.wanderscope.trip.users.api.request.ChangeRoleRequest
import cz.cvut.fit.steuejan.wanderscope.trip.users.api.request.InviteUserRequest
import cz.cvut.fit.steuejan.wanderscope.trip.users.repository.TripUserRepository
import kotlinx.coroutines.delay

class TripUsersAddEditFragmentVM(
    private val tripUserRepository: TripUserRepository
) : BaseViewModel() {

    private var purpose = Purpose.ADD
    private var tripId: Int? = null
    private var whomId: Int? = null

    val title = MutableLiveData<Int>()
    val subtitle = MutableLiveData<String>()
    val checkedRole = MutableLiveData<Int>()
    val username = MutableLiveData<String>()

    val usernameVisibility = MutableLiveData<Boolean>()
    val adminOptionVisibility = MutableLiveData(false)
    val noneOptionVisibility = MutableLiveData(false)

    val validateUsername = username.switchMapSuspend {
        if (purpose == Purpose.EDIT) {
            return@switchMapSuspend OK
        }
        validator.validateUsername(it)
    }

    val requestIsSuccess = AnySingleLiveEvent()

    val enableSubmit = ValidationMediator(validateUsername)

    val loading = LoadingMutableLiveData()

    fun init(purpose: Purpose, userRole: UserRole, tripId: Int, subtitle: String, whomId: Int) {
        this.purpose = purpose
        this.tripId = tripId
        this.subtitle.value = subtitle
        this.whomId = if (whomId == -1) null else whomId
        if (purpose == Purpose.ADD) {
            title.value = R.string.add_your_travel_mate
            usernameVisibility.value = true
            adminOptionVisibility.value = userRole == UserRole.ADMIN
            checkedRole.value = R.id.addUsersRoleEditor
        } else {
            title.value = R.string.change_user_role
            usernameVisibility.value = false
            adminOptionVisibility.value = true
            noneOptionVisibility.value = true
            checkedRole.value = userRoleToCheckedItem(userRole)
            username.value = "Why do programmers always mix up Halloween and Christmas? Because Oct 31 equals Dec 25."
        }
    }

    private fun checkedRadioToUserRole(@IdRes checkedId: Int?): UserRole? {
        checkedId ?: return null
        return when (checkedRole.value) {
            R.id.addUsersRoleAdmin -> UserRole.ADMIN
            R.id.addUsersRoleEditor -> UserRole.EDITOR
            R.id.addUsersRoleViewer -> UserRole.VIEWER
            R.id.addUsersRoleNone -> null
            else -> null
        }
    }

    private fun userRoleToCheckedItem(userRole: UserRole): Int {
        return when (userRole) {
            UserRole.ADMIN -> R.id.addUsersRoleAdmin
            UserRole.EDITOR -> R.id.addUsersRoleEditor
            UserRole.VIEWER -> R.id.addUsersRoleViewer
        }
    }

    fun submit() {
        when (purpose) {
            Purpose.ADD -> addUser()
            Purpose.EDIT -> decideChangeRoleRequest()
        }
    }

    private fun addUser() {
        viewModelScope.launchIO {
            val request = InviteUserRequest(
                username.value ?: return@launchIO,
                checkedRadioToUserRole(checkedRole.value) ?: return@launchIO
            )
            tripUserRepository.inviteUser(tripId ?: return@launchIO, request)
                .safeCollect(this) {
                    when (it) {
                        is Result.Cache -> TODO()
                        is Result.Failure -> addUserFailure(it.error)
                        is Result.Loading -> loading.value = true
                        is Result.Success -> requestIsSuccess.publish()
                    }
                }
        }
    }

    private suspend fun addUserFailure(error: Error) {
        loading.value = false
        delay(50) //ensure that loading is observed before error state
        when (error.reason?.status) {
            Status.NOT_FOUND -> validateUsername.value = R.string.user_not_found_error
            Status.BAD_REQUEST -> validateUsername.value = R.string.user_is_already_member
            else -> unexpectedError()
        }
    }

    private fun decideChangeRoleRequest() {
        val newRole = checkedRadioToUserRole(checkedRole.value ?: return)
        if (newRole == null) removeUser() else changeRole(newRole)
    }

    private fun removeUser() {
        viewModelScope.launchIO {
            tripUserRepository.removeUserRole(
                tripId ?: return@launchIO,
                whomId ?: return@launchIO
            ).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> changeRoleFailure(it.error)
                    is Result.Loading -> loading.value = true
                    is Result.Success -> requestIsSuccess.publish()
                }
            }
        }
    }

    private fun changeRole(newRole: UserRole) {
        viewModelScope.launchIO {
            val request = ChangeRoleRequest(whomId ?: return@launchIO, newRole)
            tripUserRepository.changeUserRole(tripId ?: return@launchIO, request)
                .safeCollect(this) {
                    when (it) {
                        is Result.Cache -> TODO()
                        is Result.Failure -> changeRoleFailure(it.error)
                        is Result.Loading -> loading.value = true
                        is Result.Success -> requestIsSuccess.publish()
                    }
                }
        }
    }

    private fun changeRoleFailure(error: Error) {
        loading.value = false
        when (error.reason?.customCode) {
            1 -> showLongSnackbar(R.string.change_role_error_1)
            2 -> showLongSnackbar(R.string.change_role_error_2)
            else -> {
                if (error.reason?.status == Status.NOT_FOUND) {
                    showLongSnackbar(R.string.user_not_found_error2)
                } else {
                    unexpectedError()
                }
            }
        }
    }

    private fun showLongSnackbar(@StringRes message: Int) {
        showSnackbar(
            SnackbarInfo(
                message,
                length = Constants.UNEXPECTED_ERROR_SNACKBAR_LENGTH,
                action = {}
            )
        )
    }
}