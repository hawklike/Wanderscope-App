package cz.cvut.fit.steuejan.wanderscope.app.retrofit

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.ApiResult
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.app.serialization.MoshiSerializer
import cz.cvut.fit.steuejan.wanderscope.app.serialization.Serializer
import cz.cvut.fit.steuejan.wanderscope.app.serialization.fromJson
import cz.cvut.fit.steuejan.wanderscope.app.util.runOrLogException
import retrofit2.Response

suspend inline fun <reified T> safeApiCall(
    serializer: Serializer<*> = MoshiSerializer(),
    crossinline call: suspend () -> Response<T>
): ApiResult<T, Error>? {
    return runOrLogException {
        //retrofit is main-safe, no need of IO dispatcher here
        with(call.invoke()) {
            val body = body()
            if (isSuccessful && body != null) {
                ApiResult.Success(body)
            } else {
                val error = serializer.fromJson<Error>(errorBody()!!.source())
                ApiResult.Failure(error!!)
            }
        }
    }
}