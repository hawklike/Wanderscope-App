package cz.cvut.fit.steuejan.wanderscope.points.activity.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.request.ActivityRequest
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.response.ActivityResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ActivityApi {

    @POST("/trip/{id}/activity")
    suspend fun createActivity(
        @Path("id") id: Int,
        @Body request: ActivityRequest
    ): Response<CreatedResponse>

    @GET("/trip/{tripId}/activity/{id}")
    suspend fun getActivity(
        @Path("tripId") tripId: Int,
        @Path("id") id: Int
    ): Response<ActivityResponse>
}