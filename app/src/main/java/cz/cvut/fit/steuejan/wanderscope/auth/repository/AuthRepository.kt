package cz.cvut.fit.steuejan.wanderscope.auth.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.withIO
import cz.cvut.fit.steuejan.wanderscope.app.session.SessionManager
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.auth.api.AuthApi
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.ForgotPasswordRequest
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.LoginRequest
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.RefreshTokenRequest
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.RegisterRequest
import cz.cvut.fit.steuejan.wanderscope.auth.api.response.AuthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import retrofit2.Response

class AuthRepository(
    private val sessionManager: SessionManager,
    private val authApi: AuthApi
) {

    suspend fun register(registerRequest: RegisterRequest): Flow<Result<AuthResponse>> {
        return login { authApi.register(registerRequest) }
    }

    suspend fun login(loginRequest: LoginRequest): Flow<Result<AuthResponse>> {
        return login { authApi.login(loginRequest) }
    }

    private suspend fun login(call: suspend () -> Response<AuthResponse>): Flow<Result<AuthResponse>> {
        return performCall(networkCall = call).onEach {
            if (it is Result.Success) {
                saveTokens(it.data)
            }
        }
    }

    suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Flow<Result<Unit>> {
        return performCall { authApi.forgotPassword(forgotPasswordRequest) }
    }

    suspend fun logout(): Flow<Result<Unit>> {
        var refreshToken: String? = null
        withIO {
            refreshToken = sessionManager.getRefreshToken()
            sessionManager.saveAccessToken(null)
            sessionManager.saveRefreshToken(null)
        }
        return refreshToken?.let {
            performCall { authApi.logout(RefreshTokenRequest(it)) }
        } ?: flowOf(Result.Failure())
    }

    fun shouldLogoutUser(): Flow<Boolean> {
        return sessionManager.shouldLogoutUser()
    }

    suspend fun isUserLoggedIn(): Boolean {
        return withIO {
            sessionManager.isUserLoggedIn()
        }
    }

    private suspend fun saveTokens(tokens: AuthResponse) {
        withIO {
            sessionManager.saveAccessToken(tokens.accessToken)
            sessionManager.saveRefreshToken(tokens.refreshToken)
        }
    }
}