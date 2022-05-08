package cz.cvut.fit.steuejan.wanderscope.app.di

import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val bussinessModule = module {
    singleOf(::InputValidator)
}