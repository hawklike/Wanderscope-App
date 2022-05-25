package cz.cvut.fit.steuejan.wanderscope.trip.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.response.MultipleAccommodationResponse
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.response.ActivitiesResponse
import cz.cvut.fit.steuejan.wanderscope.points.place.api.response.PlacesResponse
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.response.TransportsResponse
import cz.cvut.fit.steuejan.wanderscope.trip.api.request.TripRequest
import cz.cvut.fit.steuejan.wanderscope.trip.api.response.TripResponse
import cz.cvut.fit.steuejan.wanderscope.user.api.response.UsersResponse
import retrofit2.Response
import retrofit2.http.*

interface TripApi {

    @POST("/trip")
    suspend fun createTrip(@Body request: TripRequest): Response<CreatedResponse>

    @GET("/trip/{id}")
    suspend fun getTrip(@Path("id") id: Int): Response<TripResponse>

    @PUT("/trip/{id}")
    suspend fun editTrip(@Path("id") id: Int, @Body request: TripRequest): Response<Unit>

    @DELETE("/trip/{id}")
    suspend fun deleteTrip(@Path("id") id: Int): Response<Unit>

    @GET("/trip/{id}/transports")
    suspend fun getTransports(@Path("id") id: Int): Response<TransportsResponse>

    @GET("/trip/{id}/listAccommodation")
    suspend fun getAccommodation(@Path("id") id: Int): Response<MultipleAccommodationResponse>

    @GET("/trip/{id}/places")
    suspend fun getPlaces(@Path("id") id: Int): Response<PlacesResponse>

    @GET("/trip/{id}/activities")
    suspend fun getActivities(@Path("id") id: Int): Response<ActivitiesResponse>

    @GET("/trip/{id}/users")
    suspend fun getUsers(@Path("id") id: Int): Response<UsersResponse>

    @POST("/trip/{id}/leave")
    suspend fun leaveTrip(@Path("id") id: Int): Response<Unit>
}