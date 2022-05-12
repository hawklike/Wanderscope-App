package cz.cvut.fit.steuejan.wanderscope.trip.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.trip.api.request.TripRequest
import cz.cvut.fit.steuejan.wanderscope.trip.api.response.TripResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TripApi {

    @POST("/trip")
    suspend fun createTrip(@Body request: TripRequest): Response<CreatedResponse>

    @GET("/trip/{id}")
    suspend fun getTrip(@Path("id") id: Int): Response<TripResponse>


}