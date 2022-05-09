package cz.cvut.fit.steuejan.wanderscope.trips.api

import cz.cvut.fit.steuejan.wanderscope.trips.api.response.TripsResponse
import cz.cvut.fit.steuejan.wanderscope.trips.model.TripsScope
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TripsApi {

    @GET("/trips")
    suspend fun getTrips(
        @Query("scope") scope: TripsScope,
        @Query("date") date: String
    ): Response<TripsResponse>
}