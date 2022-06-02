package cz.cvut.fit.steuejan.wanderscope.app.util

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.isHttpSuccessful
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.ApiResult
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.UnspecifiedError
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.safeApiCall
import cz.cvut.fit.steuejan.wanderscope.app.serialization.MoshiSerializer
import cz.cvut.fit.steuejan.wanderscope.app.serialization.Serializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

inline fun <reified T> performCall(
    serializer: Serializer<*> = MoshiSerializer(),
    crossinline networkCall: suspend () -> Response<T>
) = flow {
    emit(Result.Loading)
    when (val apiResult = safeApiCall(serializer, call = networkCall)) {
        is ApiResult.Success -> emit(Result.Success(apiResult.payload))
        is ApiResult.Failure -> emit(Result.Failure(apiResult.error))
        null -> emit(Result.Failure(UnspecifiedError))
    }
}

//Retrofit returns null body when 204 NoContent is retrieved
//even though Response<Unit> is specified.
//This method resolves the bug.
fun Flow<Result<Unit>>.toUnitIfSuccess(): Flow<Result<Unit>> {
    return map {
        if (it is Result.Failure && it.error.httpCode.isHttpSuccessful()) {
            Result.Success(Unit)
        } else {
            it
        }
    }
}