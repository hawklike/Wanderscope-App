package cz.cvut.fit.steuejan.wanderscope.document.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.document.api.request.DocumentMetadataRequest
import cz.cvut.fit.steuejan.wanderscope.document.api.response.DocumentsMetadataResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface DocumentApi {

    @GET("/trip/{tripId}/documents")
    suspend fun getTripDocuments(@Path("tripId") tripId: Int): Response<DocumentsMetadataResponse>

    @GET("/trip/{tripId}/{pointType}/{pointId}/documents")
    suspend fun getPointDocuments(
        @Path("tripId") tripId: Int,
        @Path("pointType") pointType: String,
        @Path("pointId") pointId: Int
    ): Response<DocumentsMetadataResponse>

    @POST("/trip/{tripId}/document")
    suspend fun postTripDocumentMetadata(
        @Path("tripId") tripId: Int,
        @Body request: DocumentMetadataRequest
    ): Response<CreatedResponse>

    @Multipart
    @POST("/trip/{tripId}/document/{id}/data")
    suspend fun postTripDocument(
        @Path("tripId") tripId: Int,
        @Path("id") documentId: Long,
        @Part file: MultipartBody.Part
    ): Response<Unit>

    @POST("trip/{tripId}/{poinType}/{pointId}/document")
    suspend fun postPointDocumentMetadata(
        @Path("tripId") tripId: Int,
        @Path("pointType") pointType: String,
        @Path("pointId") pointId: Int,
        @Body request: DocumentMetadataRequest
    ): Response<CreatedResponse>
}