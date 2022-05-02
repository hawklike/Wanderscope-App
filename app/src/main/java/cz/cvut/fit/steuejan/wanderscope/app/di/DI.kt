package cz.cvut.fit.steuejan.wanderscope.app.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun initKoin(appContext: Context) {
    startKoin {
        androidContext(appContext)
        modules(allModules)
    }
}

private val allModules = listOf(
    serializerModule,
    networkModule,
    viewModelModule
)