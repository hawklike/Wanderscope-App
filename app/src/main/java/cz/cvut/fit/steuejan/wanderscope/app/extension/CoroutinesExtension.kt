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