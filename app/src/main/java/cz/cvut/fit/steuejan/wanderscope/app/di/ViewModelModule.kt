package cz.cvut.fit.steuejan.wanderscope.app.di

import cz.cvut.fit.steuejan.wanderscope.AccountFragmentVM
import cz.cvut.fit.steuejan.wanderscope.MainActivityVM
import cz.cvut.fit.steuejan.wanderscope.auth.forgot_password.ForgotPasswordFragmentVM
import cz.cvut.fit.steuejan.wanderscope.auth.login.LoginFragmentVM
import cz.cvut.fit.steuejan.wanderscope.auth.register.RegisterFragmentVM
import cz.cvut.fit.steuejan.wanderscope.home.HomeFragmentVM
import cz.cvut.fit.steuejan.wanderscope.trips.TripsFragmentVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainActivityVM)
    viewModelOf(::HomeFragmentVM)
    viewModelOf(::AccountFragmentVM)
    viewModelOf(::LoginFragmentVM)
    viewModelOf(::RegisterFragmentVM)
    viewModelOf(::ForgotPasswordFragmentVM)
    viewModelOf(::TripsFragmentVM)
}