package cz.cvut.fit.steuejan.wanderscope.trip.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.trip.api.request.TripRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TripApi {

    @POST("/trip")
    suspend fun createTrip(@Body request: TripRequest): Response<CreatedResponse>
}