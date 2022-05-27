package cz.cvut.fit.steuejan.wanderscope.account.api

import cz.cvut.fit.steuejan.wanderscope.account.api.response.AccountResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST


interface AccountApi {

    @GET("/account")
    suspend fun getAccount(): Response<AccountResponse>

    @POST("account/logoutAll")
    suspend fun logoutAll(): Response<Unit>
}
