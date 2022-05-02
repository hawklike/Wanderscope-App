package cz.cvut.fit.steuejan.wanderscope.auth.api

import cz.cvut.fit.steuejan.wanderscope.auth.api.request.LoginRequest
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.RefreshTokenRequest
import cz.cvut.fit.steuejan.wanderscope.auth.api.response.AuthResponse
import cz.cvut.fit.steuejan.wanderscope.auth.model.AuthFlow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("/auth/login")
    suspend fun login(
        @Body request: LoginRequest,
        @Query("flow") flow: AuthFlow = AuthFlow.EMAIL
    ): Response<AuthResponse>

    @POST("/auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<AuthResponse>
}