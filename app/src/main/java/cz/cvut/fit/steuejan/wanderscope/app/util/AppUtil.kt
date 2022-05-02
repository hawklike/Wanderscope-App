package cz.cvut.fit.steuejan.wanderscope.app.util

import cz.cvut.fit.steuejan.wanderscope.BuildConfig
import cz.cvut.fit.steuejan.wanderscope.app.common.Flavor
import timber.log.Timber

inline fun <T> runOrLogException(call: () -> T): T? {
    return runCatching(call).getOrElse {
        Timber.e(it)
        null
    }
}

inline fun <T1 : Any, T2 : Any, R : Any> multipleLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

fun isDebuggable(): Boolean {
    return BuildConfig.DEBUG && BuildConfig.FLAVOR == Flavor.STAGING.env
}