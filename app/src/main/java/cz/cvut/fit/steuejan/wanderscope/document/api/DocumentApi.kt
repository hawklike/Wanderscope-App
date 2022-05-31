package cz.cvut.fit.steuejan.wanderscope.document.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.document.api.request.DocumentMetadataRequest
import cz.cvut.fit.steuejan.wanderscope.document.api.response.DocumentsMetadataResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
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
        @Path("id") documentId: Int,
        @Part file: MultipartBody.Part
    ): Response<Unit>

    @POST("trip/{tripId}/{pointType}/{pointId}/document")
    suspend fun postPointDocumentMetadata(
        @Path("tripId") tripId: Int,
        @Path("pointType") pointType: String,
        @Path("pointId") pointId: Int,
        @Body request: DocumentMetadataRequest
    ): Response<CreatedResponse>

    @Multipart
    @POST("trip/{tripId}/{pointType}/{pointId}/document/{id}/data")
    suspend fun postPointDocument(
        @Path("tripId") tripId: Int,
        @Path("pointType") pointType: String,
        @Path("pointId") pointId: Int,
        @Path("id") documentId: Int,
        @Part file: MultipartBody.Part
    ): Response<Unit>

    @Streaming
    @GET("/trip/{tripId}/document/{id}/data")
    suspend fun getTripDocument(
        @Path("tripId") tripId: Int,
        @Path("id") id: Int,
        @Header("Wanderscope-Document-Key") key: String? = null
    ): Response<ResponseBody>

    @Streaming
    @GET("trip/{tripId}/{pointType}/{pointId}/document/{id}/data")
    suspend fun getTripPointDocument(
        @Path("tripId") tripId: Int,
        @Path("pointType") pointType: String,
        @Path("pointId") pointId: Int,
        @Path("id") documentId: Int,
        @Header("Wanderscope-Document-Key") key: String? = null
    ): Response<ResponseBody>

    @DELETE("/trip/{tripId}/document/{id}")
    suspend fun deleteTripDocument(
        @Path("tripId") tripId: Int,
        @Path("id") id: Int
    ): Response<Unit>

    @DELETE("/trip/{tripId}/{pointType}/{pointId}/document/{id}")
    suspend fun deleteTripPointDocument(
        @Path("tripId") tripId: Int,
        @Path("pointType") pointType: String,
        @Path("pointId") pointId: Int,
        @Path("id") documentId: Int
    ): Response<Unit>
}