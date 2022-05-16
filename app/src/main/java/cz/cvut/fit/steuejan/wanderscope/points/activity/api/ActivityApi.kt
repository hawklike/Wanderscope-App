package cz.cvut.fit.steuejan.wanderscope.points.activity.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.request.ActivityRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ActivityApi {

    @POST("/trip/{id}/activity")
    suspend fun createActivity(
        @Path("id") id: Int,
        @Body request: ActivityRequest
    ): Response<CreatedResponse>
}