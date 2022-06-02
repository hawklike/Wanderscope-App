package cz.cvut.fit.steuejan.wanderscope.points.place.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.points.place.api.request.PlaceRequest
import cz.cvut.fit.steuejan.wanderscope.points.place.api.response.PlaceResponse
import retrofit2.Response
import retrofit2.http.*

interface PlaceApi {

    @POST("/trip/{id}/place")
    suspend fun createPlace(
        @Path("id") id: Int,
        @Body request: PlaceRequest,
        @Query("wiki") wikiSearch: String?
    ): Response<CreatedResponse>

    @GET("/trip/{tripId}/place/{id}")
    suspend fun getPlace(
        @Path("tripId") tripId: Int,
        @Path("id") id: Int
    ): Response<PlaceResponse>

    @PUT("/trip/{tripId}/place/{id}")
    suspend fun editPlace(
        @Path("tripId") tripId: Int,
        @Path("id") id: Int,
        @Body request: PlaceRequest,
        @Query("wiki") wikiSearch: String?
    ): Response<Unit>

    @DELETE("/trip/{tripId}/place/{id}")
    suspend fun deletePlace(
        @Path("tripId") tripId: Int,
        @Path("id") id: Int
    ): Response<Unit>
}