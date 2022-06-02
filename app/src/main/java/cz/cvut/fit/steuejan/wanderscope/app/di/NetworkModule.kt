package cz.cvut.fit.steuejan.wanderscope.app.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import cz.cvut.fit.steuejan.wanderscope.BuildConfig
import cz.cvut.fit.steuejan.wanderscope.account.api.AccountApi
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.AuthInterceptor
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.TokenAuthenticator
import cz.cvut.fit.steuejan.wanderscope.app.serialization.Serializer
import cz.cvut.fit.steuejan.wanderscope.app.session.SessionManager
import cz.cvut.fit.steuejan.wanderscope.app.session.SessionManagerImpl
import cz.cvut.fit.steuejan.wanderscope.app.util.isDebuggable
import cz.cvut.fit.steuejan.wanderscope.auth.api.AuthApi
import cz.cvut.fit.steuejan.wanderscope.document.api.DocumentApi
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.AccommodationApi
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.ActivityApi
import cz.cvut.fit.steuejan.wanderscope.points.place.api.PlaceApi
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.TransportApi
import cz.cvut.fit.steuejan.wanderscope.trip.api.TripApi
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.ExpenseApi
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.ItineraryApi
import cz.cvut.fit.steuejan.wanderscope.trip.users.api.TripUserApi
import cz.cvut.fit.steuejan.wanderscope.trips.api.TripsApi
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val WITHOUT_AUTH = "noAuth"

val networkModule = module {
    singleOf(::SessionManagerImpl) { bind<SessionManager>() }
    singleOf(::AuthInterceptor)
    singleOf(::provideChucker)

    singleOf(::provideMoshiConverterFactory)
    singleOf(::provideRetrofitBuilder)
    singleOf(::provideOkHttpClientBuilder)

    single(named(WITHOUT_AUTH)) { provideOkHttpClientBuilder(get()).build() }
    single(named(WITHOUT_AUTH)) { provideRetrofit(get(named(WITHOUT_AUTH)), get()) }

    single { provideApi<AuthApi>(get(named(WITHOUT_AUTH))) }

    singleOf(::TokenAuthenticator)
    singleOf(::provideOkHttpClientWithAuth)
    singleOf(::provideRetrofit)

    single { provideApi<AccountApi>(get()) }
    single { provideApi<TripsApi>(get()) }
    single { provideApi<TripApi>(get()) }
    single { provideApi<AccommodationApi>(get()) }
    single { provideApi<ActivityApi>(get()) }
    single { provideApi<TransportApi>(get()) }
    single { provideApi<PlaceApi>(get()) }
    single { provideApi<DocumentApi>(get()) }
    single { provideApi<ItineraryApi>(get()) }
    single { provideApi<TripUserApi>(get()) }
    single { provideApi<ExpenseApi>(get()) }
}

private fun provideRetrofitBuilder(converterFactory: retrofit2.Converter.Factory): Retrofit.Builder {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(converterFactory)
}

private fun provideMoshiConverterFactory(serializer: Serializer<Moshi>): retrofit2.Converter.Factory {
    return MoshiConverterFactory.create(serializer.getSerializer()).asLenient()
}

private fun provideChucker(context: Context): ChuckerInterceptor? {
    if (isDebuggable()) {
        return ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
    }
    return null
}

fun provideOkHttpClientBuilder(chucker: ChuckerInterceptor?): OkHttpClient.Builder {
    return OkHttpClient.Builder()
        .readTimeout(Constants.API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(Constants.API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .apply { chucker?.let { addInterceptor(it) } }
}

fun provideOkHttpClientWithAuth(
    okHttpBuilder: OkHttpClient.Builder,
    authInterceptor: AuthInterceptor,
    tokenAuthenticator: TokenAuthenticator
): OkHttpClient {
    return okHttpBuilder
        .addInterceptor(authInterceptor)
        .authenticator(tokenAuthenticator)
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient, retrofitBuilder: Retrofit.Builder): Retrofit {
    return retrofitBuilder
        .client(okHttpClient)
        .build()
}

inline fun <reified T> provideApi(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}