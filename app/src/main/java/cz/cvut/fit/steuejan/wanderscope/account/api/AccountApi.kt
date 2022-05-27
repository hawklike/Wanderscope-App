package cz.cvut.fit.steuejan.wanderscope.account.api

import cz.cvut.fit.steuejan.wanderscope.account.api.request.ChangeDisplayNameRequest
import cz.cvut.fit.steuejan.wanderscope.account.api.request.ChangePasswordRequest
import cz.cvut.fit.steuejan.wanderscope.account.api.response.AccountResponse
import cz.cvut.fit.steuejan.wanderscope.auth.api.response.AuthResponse
import retrofit2.Response
import retrofit2.http.*


interface AccountApi {

    @GET("/account")
    suspend fun getAccount(): Response<AccountResponse>

    @POST("/account/logoutAll")
    suspend fun logoutAll(): Response<Unit>

    @DELETE("/account")
    suspend fun deleteAccount(): Response<Unit>

    @POST("/account/changePassword")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<AuthResponse>

    @PUT("/account/name")
    suspend fun changeDisplayName(@Body request: ChangeDisplayNameRequest): Response<Unit>
}
