package cz.cvut.fit.steuejan.wanderscope.points.transport.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.request.TransportRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface TransportApi {

    @POST("/trip/{id}/transport")
    suspend fun createTransport(
        @Path("id") id: Int,
        @Body request: TransportRequest
    ): Response<CreatedResponse>
}