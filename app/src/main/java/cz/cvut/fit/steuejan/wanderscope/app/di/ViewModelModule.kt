package cz.cvut.fit.steuejan.wanderscope.app.di

import cz.cvut.fit.steuejan.wanderscope.FirstFragmentVM
import cz.cvut.fit.steuejan.wanderscope.MainActivityVM
import cz.cvut.fit.steuejan.wanderscope.SecondFragmentVM
import cz.cvut.fit.steuejan.wanderscope.auth.intro.IntroFragmentVM
import cz.cvut.fit.steuejan.wanderscope.auth.login.LoginFragmentVM
import cz.cvut.fit.steuejan.wanderscope.auth.register.RegisterFragmentVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainActivityVM)
    viewModelOf(::FirstFragmentVM)
    viewModelOf(::SecondFragmentVM)
    viewModelOf(::IntroFragmentVM)
    viewModelOf(::LoginFragmentVM)
    viewModelOf(::RegisterFragmentVM)
}