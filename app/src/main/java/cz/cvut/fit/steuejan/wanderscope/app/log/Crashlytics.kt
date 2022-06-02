package cz.cvut.fit.steuejan.wanderscope.app.log

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import cz.cvut.fit.steuejan.wanderscope.BuildConfig
import cz.cvut.fit.steuejan.wanderscope.app.util.isDebuggable

fun initFirebaseCrashlytics() {
    if (isDebuggable()) {
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(false)
    }

    Firebase.crashlytics.apply {
        setCustomKey(FIREBASE_FLAVOR_KEY, BuildConfig.FLAVOR)
        setCustomKey(FIREBASE_DEBUG_KEY, BuildConfig.DEBUG)
    }
}

const val FIREBASE_FLAVOR_KEY = "env"
const val FIREBASE_DEBUG_KEY = "debug"