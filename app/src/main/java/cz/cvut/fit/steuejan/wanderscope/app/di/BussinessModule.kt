package cz.cvut.fit.steuejan.wanderscope.app.di

import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator
import cz.cvut.fit.steuejan.wanderscope.document.bussiness.FileInfoParser
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val bussinessModule = module {
    singleOf(::InputValidator)
    single { FileInfoParser(androidContext()) }
}