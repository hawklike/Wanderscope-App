package cz.cvut.fit.steuejan.wanderscope.account.api

import cz.cvut.fit.steuejan.wanderscope.account.api.response.AccountResponse
import retrofit2.Response
import retrofit2.http.GET


interface AccountApi {

    @GET("/account")
    suspend fun getAccount(): Response<AccountResponse>
}
