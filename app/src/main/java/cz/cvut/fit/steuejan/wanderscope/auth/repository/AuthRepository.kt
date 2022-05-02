package cz.cvut.fit.steuejan.wanderscope.auth.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.withIO
import cz.cvut.fit.steuejan.wanderscope.app.session.SessionManager
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.auth.api.AuthApi
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.LoginRequest
import cz.cvut.fit.steuejan.wanderscope.auth.api.response.AuthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class AuthRepository(
    private val sessionManager: SessionManager,
    private val authApi: AuthApi
) {
    suspend fun logout() {
        withIO {
            sessionManager.saveAccessToken(null)
            sessionManager.saveRefreshToken(null)
        }
    }

    suspend fun isUserLoggedIn(): Boolean {
        return withIO {
            sessionManager.isUserLoggedIn()
        }
    }

    suspend fun login(loginRequest: LoginRequest): Flow<Result<AuthResponse>> {
        return performCall { authApi.login(loginRequest) }.onEach {
            if (it is Result.Success) {
                sessionManager.saveAccessToken(it.data.accessToken)
                sessionManager.saveRefreshToken(it.data.refreshToken)
            }
        }
    }
}