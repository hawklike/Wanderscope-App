package cz.cvut.fit.steuejan.wanderscope.points.accommodation.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.request.AccommodationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AccommodationApi {

    @POST("/trip/{id}/accommodation")
    suspend fun createAccommodation(
        @Path("id") id: Int,
        @Body request: AccommodationRequest
    ): Response<CreatedResponse>
}