package cz.cvut.fit.steuejan.wanderscope.points.transport.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.request.TransportRequest
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.response.TransportResponse
import retrofit2.Response
import retrofit2.http.*

interface TransportApi {

    @POST("/trip/{id}/transport")
    suspend fun createTransport(
        @Path("id") id: Int,
        @Body request: TransportRequest
    ): Response<CreatedResponse>

    @GET("/trip/{tripId}/transport/{id}")
    suspend fun getTransport(
        @Path("tripId") tripId: Int,
        @Path("id") id: Int
    ): Response<TransportResponse>

    @PUT("/trip/{tripId}/transport/{id}")
    suspend fun editTransport(
        @Path("tripId") tripId: Int,
        @Path("id") id: Int,
        @Body request: TransportRequest
    ): Response<Unit>

    @DELETE("/trip/{tripId}/transport/{id}")
    suspend fun deleteTransport(
        @Path("tripId") tripId: Int,
        @Path("id") id: Int
    ): Response<Unit>
}