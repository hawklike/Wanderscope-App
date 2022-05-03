package cz.cvut.fit.steuejan.wanderscope.auth.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.withIO
import cz.cvut.fit.steuejan.wanderscope.app.session.SessionManager
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.auth.api.AuthApi
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.LoginRequest
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.RefreshTokenRequest
import cz.cvut.fit.steuejan.wanderscope.auth.api.response.AuthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

class AuthRepository(
    private val sessionManager: SessionManager,
    private val authApi: AuthApi
) {

    fun shouldLogoutUser(): Flow<Boolean> {
        return sessionManager.shouldLogoutUser()
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

    suspend fun isUserLoggedIn(): Boolean {
        return withIO {
            sessionManager.isUserLoggedIn()
        }
    }

    suspend fun login(loginRequest: LoginRequest): Flow<Result<AuthResponse>> {
        return performCall { authApi.login(loginRequest) }.onEach {
            if (it is Result.Success) {
                saveTokens(it.data)
            }
        }
    }

    private suspend fun saveTokens(tokens: AuthResponse) {
        withIO {
            sessionManager.saveAccessToken(tokens.accessToken)
            sessionManager.saveRefreshToken(tokens.refreshToken)
        }
    }
}