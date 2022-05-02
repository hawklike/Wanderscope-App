package cz.cvut.fit.steuejan.wanderscope.app.session

interface SessionManager {
    fun saveAccessToken(token: String?)
    fun getAccessToken(): String?
    fun saveRefreshToken(token: String?)
    fun getRefreshToken(): String?
}