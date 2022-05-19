package cz.cvut.fit.steuejan.wanderscope.points.accommodation.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.request.AccommodationRequest
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.response.AccommodationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
}