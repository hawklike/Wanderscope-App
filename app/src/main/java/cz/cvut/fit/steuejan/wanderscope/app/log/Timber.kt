package cz.cvut.fit.steuejan.wanderscope.app.log

import android.util.Log.*
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import cz.cvut.fit.steuejan.wanderscope.app.util.isDebuggable
import timber.log.Timber

fun initTimber() {
    if (isDebuggable()) {
        Timber.plant(Timber.DebugTree())
    } else {
        Timber.plant(CrashReportingTree())
    }
}

private class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority in listOf(VERBOSE, DEBUG, WARN)) {
            return
        }

        t?.let {
            Firebase.crashlytics.log(message)
            Firebase.crashlytics.recordException(it)
        }
    }
}