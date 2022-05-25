package cz.cvut.fit.steuejan.wanderscope.app.di

import cz.cvut.fit.steuejan.wanderscope.account.repository.AccountRepository
import cz.cvut.fit.steuejan.wanderscope.auth.repository.AuthRepository
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.repository.AccommodationRepository
import cz.cvut.fit.steuejan.wanderscope.points.activity.repository.ActivityRepository
import cz.cvut.fit.steuejan.wanderscope.points.place.repository.PlaceRepository
import cz.cvut.fit.steuejan.wanderscope.points.transport.repository.TransportRepository
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.repository.ItineraryRepository
import cz.cvut.fit.steuejan.wanderscope.trip.repository.TripRepository
import cz.cvut.fit.steuejan.wanderscope.trips.repository.TripsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::AuthRepository)
    singleOf(::AccountRepository)
    singleOf(::TripsRepository)
    singleOf(::TripRepository)
    singleOf(::AccommodationRepository)
    singleOf(::ActivityRepository)
    singleOf(::TransportRepository)
    singleOf(::PlaceRepository)
    singleOf(::ItineraryRepository)
}