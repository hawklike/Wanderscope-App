package cz.cvut.fit.steuejan.wanderscope.trip.users.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.app.util.toUnitIfSuccess
import cz.cvut.fit.steuejan.wanderscope.trip.users.api.TripUserApi
import cz.cvut.fit.steuejan.wanderscope.trip.users.api.request.ChangeRoleRequest
import cz.cvut.fit.steuejan.wanderscope.trip.users.api.request.InviteUserRequest
import kotlinx.coroutines.flow.Flow

class TripUserRepository(private val tripUserApi: TripUserApi) {

    suspend fun inviteUser(tripId: Int, inviteRequest: InviteUserRequest): Flow<Result<Unit>> {
        return performCall { tripUserApi.inviteUser(tripId, inviteRequest) }.toUnitIfSuccess()
    }

    suspend fun changeUserRole(tripId: Int, request: ChangeRoleRequest): Flow<Result<Unit>> {
        return performCall { tripUserApi.changeUserRole(tripId, request) }.toUnitIfSuccess()
    }

    suspend fun removeUserRole(tripId: Int, userToRemoveId: Int): Flow<Result<Unit>> {
        return performCall { tripUserApi.removeUser(tripId, userToRemoveId) }.toUnitIfSuccess()
    }
}