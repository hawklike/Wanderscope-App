package cz.cvut.fit.steuejan.wanderscope.app.retrofit.response

@Suppress("unused")
enum class Status {
    SUCCESS, ACCEPTED, NO_CONTENT, CREATED,
    UNAUTHORIZED, BAD_REQUEST, FORBIDDEN, INTERNAL_ERROR, NOT_FOUND;

    fun isSuccess() = this in listOf(SUCCESS, ACCEPTED, NO_CONTENT, CREATED)
}