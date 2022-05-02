package cz.cvut.fit.steuejan.wanderscope.app.retrofit.response

sealed class ApiResult<out T, out E> {
    data class Success<T>(val payload: T) : ApiResult<T, Nothing>()
    data class Failure(val error: Error = Error()) : ApiResult<Nothing, Error>()
}