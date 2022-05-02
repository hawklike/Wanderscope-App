package cz.cvut.fit.steuejan.wanderscope.app.retrofit

import cz.cvut.fit.steuejan.wanderscope.app.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val authRequest = chain.request().newBuilder().apply {
            sessionManager.getAccessToken()?.let {
                header("Authorization", "Bearer $it")
            }
        }.build()
        return chain.proceed(authRequest)
    }
}