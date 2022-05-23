package cz.cvut.fit.steuejan.wanderscope.points.accommodation.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.request.AccommodationRequest
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.response.AccommodationResponse
import retrofit2.Response
import retrofit2.http.*

interface AccommodationApi {

    @POST("/trip/{id}/accommodation")
    suspend fun createAccommodation(
        @Path("id") id: Int,
        @Body request: AccommodationRequest
    ): Response<CreatedResponse>

    @GET("/trip/{tripId}/accommodation/{id}")
    suspend fun getAccommodation(
        @Path("tripId") tripId: Int,
        @Path("id") id: Int
    ): Response<AccommodationResponse>

    @PUT("/trip/{tripId}/accommodation/{id}")
    suspend fun editAccommodation(
        @Path("tripId") tripId: Int,
        @Path("id") id: Int,
        @Body request: AccommodationRequest
    ): Response<Unit>
}