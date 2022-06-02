package cz.cvut.fit.steuejan.wanderscope.app.retrofit

import cz.cvut.fit.steuejan.wanderscope.app.extension.withIO
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.ApiResult
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.app.session.SessionManager
import cz.cvut.fit.steuejan.wanderscope.auth.api.AuthApi
import cz.cvut.fit.steuejan.wanderscope.auth.api.request.RefreshTokenRequest
import cz.cvut.fit.steuejan.wanderscope.auth.api.response.AuthResponse
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return authenticateWithRefreshToken(response)
    }

    @Synchronized
    private fun authenticateWithRefreshToken(response: Response): Request? {
        return runBlocking {
            val result = refreshToken() ?: run {
                sessionManager.requestLogout()
                return@runBlocking null
            }

            val accessToken: String
            if (result is ApiResult.Success) {
                saveTokens(result.payload)
                accessToken = result.payload.accessToken
            } else {
                if (result is ApiResult.Failure && result.error.reason != null) {
                    sessionManager.requestLogout()
                }
                return@runBlocking null
            }
            createNewRequest(response, accessToken)
        }
    }

    private fun createNewRequest(response: Response, accessToken: String): Request {
        return response.request().newBuilder().header(
            AuthInterceptor.AUTH_HEADER, AuthInterceptor.bearerToken(accessToken)
        ).build()
    }

    private suspend fun refreshToken(): ApiResult<AuthResponse, Error>? {
        val refreshToken = withIO { sessionManager.getRefreshToken() } ?: return null
        return safeApiCall { authApi.refreshToken(RefreshTokenRequest(refreshToken)) }
    }

    private suspend fun saveTokens(tokens: AuthResponse?) {
        withIO {
            sessionManager.saveAccessToken(tokens?.accessToken)
            sessionManager.saveRefreshToken(tokens?.refreshToken)
        }
    }
}