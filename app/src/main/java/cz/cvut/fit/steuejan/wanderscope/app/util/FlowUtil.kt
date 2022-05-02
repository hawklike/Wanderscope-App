package cz.cvut.fit.steuejan.wanderscope.app.util

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.ApiResult
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Status
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.safeApiCall
import cz.cvut.fit.steuejan.wanderscope.app.serialization.MoshiSerializer
import cz.cvut.fit.steuejan.wanderscope.app.serialization.Serializer
import kotlinx.coroutines.flow.flow
import retrofit2.Response

inline fun <reified T> performCall(
    serializer: Serializer<*> = MoshiSerializer(),
    crossinline networkCall: suspend () -> Response<T>
) = flow {
    emit(Result.Loading)
    when (val apiResult = safeApiCall(serializer, call = networkCall)) {
        is ApiResult.Success -> emit(Result.Success(apiResult.payload))
        is ApiResult.Failure -> emit(Result.Failure(apiResult.error))
        null -> emit(Result.Failure(Error(Status.INTERNAL_ERROR, "Something went wrong.")))
    }
}