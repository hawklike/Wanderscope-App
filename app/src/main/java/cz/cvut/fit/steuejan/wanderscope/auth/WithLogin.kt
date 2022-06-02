package cz.cvut.fit.steuejan.wanderscope.auth

interface WithLogin {
    fun login()
    fun logout()
}