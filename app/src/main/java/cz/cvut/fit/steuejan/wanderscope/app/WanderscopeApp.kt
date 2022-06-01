package cz.cvut.fit.steuejan.wanderscope.app

import android.app.Application
import com.yariksoffice.lingver.Lingver
import cz.cvut.fit.steuejan.wanderscope.app.di.initKoin
import cz.cvut.fit.steuejan.wanderscope.app.log.initFirebaseCrashlytics
import cz.cvut.fit.steuejan.wanderscope.app.log.initTimber
import java.util.*

@Suppress("unused") //used in manifest
class WanderscopeApp : Application() {

    override fun onCreate() {
        super.onCreate()

        setLanguage()
        initFirebaseCrashlytics()
        initTimber()
        initKoin(this)
    }

    private fun setLanguage() {
        val language = Locale.getDefault().language
        Lingver.init(this, language)
    }
}