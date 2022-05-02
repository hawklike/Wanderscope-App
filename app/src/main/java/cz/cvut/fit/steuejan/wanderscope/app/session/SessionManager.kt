package cz.cvut.fit.steuejan.wanderscope.app.session

import kotlinx.coroutines.flow.Flow

interface SessionManager {
    fun saveAccessToken(token: String?)
    fun getAccessToken(): String?
    fun saveRefreshToken(token: String?)
    fun getRefreshToken(): String?
    fun isUserLoggedIn(): Boolean
    suspend fun requestLogout()
    fun shouldLogoutUser(): Flow<Boolean>
}