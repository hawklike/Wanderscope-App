package cz.cvut.fit.steuejan.wanderscope.trip.users.api

import cz.cvut.fit.steuejan.wanderscope.trip.users.api.request.InviteUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface TripUserApi {

    @POST("/trip/{id}/leave")
    suspend fun leaveTrip(@Path("id") id: Int): Response<Unit>

    @POST("/trip/{id}/invite")
    suspend fun inviteUser(
        @Path("id") id: Int,
        @Body request: InviteUserRequest
    ): Response<Unit>
}