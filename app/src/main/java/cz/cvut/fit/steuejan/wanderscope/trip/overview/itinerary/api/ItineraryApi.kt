package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api

import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.api.response.TripItineraryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ItineraryApi {

    @GET("/trip/{id}/itinerary")
    suspend fun getItinerary(@Path("id") id: Int): Response<TripItineraryResponse>
}