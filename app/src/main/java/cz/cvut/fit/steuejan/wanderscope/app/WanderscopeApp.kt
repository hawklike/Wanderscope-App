package cz.cvut.fit.steuejan.wanderscope.app

import android.app.Application
import cz.cvut.fit.steuejan.wanderscope.app.di.initKoin
import cz.cvut.fit.steuejan.wanderscope.app.log.initFirebaseCrashlytics
import cz.cvut.fit.steuejan.wanderscope.app.log.initTimber

@Suppress("unused") //used in manifest
class WanderscopeApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initFirebaseCrashlytics()
        initTimber()
        initKoin(this)
    }
}