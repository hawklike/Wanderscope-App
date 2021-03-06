package cz.cvut.fit.steuejan.wanderscope.app.di

import cz.cvut.fit.steuejan.wanderscope.MainActivityVM
import cz.cvut.fit.steuejan.wanderscope.account.AccountFragmentVM
import cz.cvut.fit.steuejan.wanderscope.account.change_password.ChangePasswordFragmentVM
import cz.cvut.fit.steuejan.wanderscope.account.display_name.DisplayNameFragmentVM
import cz.cvut.fit.steuejan.wanderscope.auth.forgot_password.ForgotPasswordFragmentVM
import cz.cvut.fit.steuejan.wanderscope.auth.login.LoginFragmentVM
import cz.cvut.fit.steuejan.wanderscope.auth.register.RegisterFragmentVM
import cz.cvut.fit.steuejan.wanderscope.document.UploadDocumentFragmentVM
import cz.cvut.fit.steuejan.wanderscope.home.HomeFragmentVM
import cz.cvut.fit.steuejan.wanderscope.map.MapFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.crud.AccommodationAddEditFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.overview.AccommodationOverviewFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.activity.crud.ActivityAddEditFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.activity.overview.ActivityOverviewFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.place.crud.PlaceAddEditFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.place.overview.PlaceOverviewFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.transport.crud.TransportAddEditFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.transport.overview.TransportOverviewFragmentVM
import cz.cvut.fit.steuejan.wanderscope.trip.crud.AddEditTripFragmentVM
import cz.cvut.fit.steuejan.wanderscope.trip.overview.TripOverviewFragmentVM
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.TripExpensesFragmentVM
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.room.crud.ExpenseRoomAddEditFragmentVM
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.TripItineraryFragmentVM
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentVM
import cz.cvut.fit.steuejan.wanderscope.trip.users.TripUsersFragmentVM
import cz.cvut.fit.steuejan.wanderscope.trip.users.crud.TripUsersAddEditFragmentVM
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
    viewModelOf(::AddEditTripFragmentVM)
    viewModelOf(::TripPagerFragmentVM)
    viewModelOf(::TripOverviewFragmentVM)
    viewModelOf(::TripItineraryFragmentVM)
    viewModelOf(::TripExpensesFragmentVM)
    viewModelOf(::AccommodationAddEditFragmentVM)
    viewModelOf(::ActivityAddEditFragmentVM)
    viewModelOf(::TransportAddEditFragmentVM)
    viewModelOf(::PlaceAddEditFragmentVM)
    viewModelOf(::TransportOverviewFragmentVM)
    viewModelOf(::PlaceOverviewFragmentVM)
    viewModelOf(::ActivityOverviewFragmentVM)
    viewModelOf(::AccommodationOverviewFragmentVM)
    viewModelOf(::TripUsersFragmentVM)
    viewModelOf(::TripUsersAddEditFragmentVM)
    viewModelOf(::ChangePasswordFragmentVM)
    viewModelOf(::DisplayNameFragmentVM)
    viewModelOf(::UploadDocumentFragmentVM)
    viewModelOf(::ExpenseRoomAddEditFragmentVM)
    viewModelOf(::MapFragmentVM)
}