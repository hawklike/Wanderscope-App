package cz.cvut.fit.steuejan.wanderscope.trip.users.api

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface TripUserApi {

    @POST("/trip/{id}/leave")
    suspend fun leaveTrip(@Path("id") id: Int): Response<Unit>


}