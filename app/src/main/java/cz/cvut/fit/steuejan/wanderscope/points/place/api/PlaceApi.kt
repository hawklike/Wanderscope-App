package cz.cvut.fit.steuejan.wanderscope.points.place.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.points.place.api.request.PlaceRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceApi {

    @POST("/trip/{id}/place")
    suspend fun createPlace(
        @Path("id") id: Int,
        @Body request: PlaceRequest
    ): Response<CreatedResponse>

    @POST("/trip/{id}/place")
    suspend fun createPlace(
        @Path("id") id: Int,
        @Query("wiki") wikiSearch: String,
        @Body request: PlaceRequest
    ): Response<CreatedResponse>
}