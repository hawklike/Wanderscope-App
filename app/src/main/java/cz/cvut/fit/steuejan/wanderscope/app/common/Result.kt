package cz.cvut.fit.steuejan.wanderscope.app.common

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Cache<out T>(val data: T) : Result<T>()
    data class Failure(val error: Error) : Result<Nothing>()
    object Loading : Result<Nothing>()
}