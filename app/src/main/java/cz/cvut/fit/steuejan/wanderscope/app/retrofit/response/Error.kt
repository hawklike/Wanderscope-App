package cz.cvut.fit.steuejan.wanderscope.app.retrofit.response

data class Error(
    val status: Status,
    val message: String,
    val code: Int? = null
)