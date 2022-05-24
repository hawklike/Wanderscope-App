package cz.cvut.fit.steuejan.wanderscope.app.extension

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.shareIn
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.launchIO(
    context: CoroutineContext = Dispatchers.IO,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launch(context, start, block)
}

suspend fun <T> withIO(
    context: CoroutineContext = Dispatchers.IO,
    block: suspend CoroutineScope.() -> T
): T {
    return withContext(context, block)
}

suspend fun <T> withDefault(
    context: CoroutineContext = Dispatchers.Default,
    block: suspend CoroutineScope.() -> T
): T {
    return withContext(context, block)
}

/**
 * It is safe to post changes to MutableLiveData via *.value*
 * because the [action] runs default in the Main Dispatcher.
 */
suspend inline fun <T> Flow<Result<T>>.safeCollect(
    scope: CoroutineScope,
    collectDispatcher: CoroutineDispatcher = Dispatchers.Main,
    crossinline action: suspend (Result<T>) -> Unit
) {
    shareIn(scope, SharingStarted.WhileSubscribed(5000), 1)
    collect {
        withContext(collectDispatcher) {
            action.invoke(it)
        }
    }
}

suspend inline fun <T> Flow<Result<T>>.fireAndForget(scope: CoroutineScope) {
    safeCollect(scope, action = {})
}