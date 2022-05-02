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

fun isDebuggable(): Boolean {
    return BuildConfig.DEBUG && BuildConfig.FLAVOR == Flavor.STAGING.env
}