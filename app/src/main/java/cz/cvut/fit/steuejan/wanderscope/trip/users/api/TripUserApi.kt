package cz.cvut.fit.steuejan.wanderscope.trip.users.api

import cz.cvut.fit.steuejan.wanderscope.trip.users.api.request.ChangeRoleRequest
import cz.cvut.fit.steuejan.wanderscope.trip.users.api.request.InviteUserRequest
import retrofit2.Response
import retrofit2.http.*

interface TripUserApi {

    @POST("/trip/{id}/leave")
    suspend fun leaveTrip(@Path("id") id: Int): Response<Unit>

    @POST("/trip/{id}/invite")
    suspend fun inviteUser(
        @Path("id") id: Int,
        @Body request: InviteUserRequest
    ): Response<Unit>

    @POST("/trip/{id}/role")
    suspend fun changeUserRole(
        @Path("id") id: Int,
        @Body request: ChangeRoleRequest
    ): Response<Unit>

    @DELETE("/trip/{id}/role")
    suspend fun removeUser(
        @Path("id") id: Int,
        @Query("userId") userToRemoveId: Int
    ): Response<Unit>

}