package cz.cvut.fit.steuejan.wanderscope.document.api

import cz.cvut.fit.steuejan.wanderscope.document.api.response.DocumentsMetadataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DocumentApi {

    @GET("/trip/{tripId}/{pointType}/{pointId}/documents")
    suspend fun getPointDocuments(
        @Path("tripId") tripId: Int,
        @Path("pointType") pointType: String,
        @Path("pointId") pointId: Int
    ): Response<DocumentsMetadataResponse>

    @GET("/trip/{tripId}/documents")
    suspend fun getTripDocuments(@Path("tripId") tripId: Int): Response<DocumentsMetadataResponse>
}