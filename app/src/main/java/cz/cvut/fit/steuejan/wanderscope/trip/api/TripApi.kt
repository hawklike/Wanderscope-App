package cz.cvut.fit.steuejan.wanderscope.trip.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.response.MultipleAccommodationResponse
import cz.cvut.fit.steuejan.wanderscope.points.activity.response.ActivitiesResponse
import cz.cvut.fit.steuejan.wanderscope.points.place.response.PlacesResponse
import cz.cvut.fit.steuejan.wanderscope.points.transport.response.TransportsResponse
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

    @GET("/trip/{id}/transports")
    suspend fun getTransports(@Path("id") id: Int): Response<TransportsResponse>

    @GET("/trip/{id}/accommodation")
    suspend fun getAccommodation(@Path("id") id: Int): Response<MultipleAccommodationResponse>

    @GET("/trip/{id}/places")
    suspend fun getPlaces(@Path("id") id: Int): Response<PlacesResponse>

    @GET("/trip/{id}/activities")
    suspend fun getActivities(@Path("id") id: Int): Response<ActivitiesResponse>

}