package cz.cvut.fit.steuejan.wanderscope.trip.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.document.response.DocumentsMetadataResponse
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.response.MultipleAccommodationResponse
import cz.cvut.fit.steuejan.wanderscope.points.activity.response.ActivitiesResponse
import cz.cvut.fit.steuejan.wanderscope.points.place.response.PlacesResponse
import cz.cvut.fit.steuejan.wanderscope.points.transport.response.TransportsResponse
import cz.cvut.fit.steuejan.wanderscope.trip.api.TripApi
import cz.cvut.fit.steuejan.wanderscope.trip.api.request.TripRequest
import cz.cvut.fit.steuejan.wanderscope.trip.api.response.TripResponse
import cz.cvut.fit.steuejan.wanderscope.user.response.UsersResponse
import kotlinx.coroutines.flow.Flow

class TripRepository(private val tripApi: TripApi) {

    suspend fun createTrip(request: TripRequest): Flow<Result<CreatedResponse>> {
        return performCall { tripApi.createTrip(request) }
    }

    suspend fun getTrip(id: Int): Flow<Result<TripResponse>> {
        return performCall { tripApi.getTrip(id) }
    }

    suspend fun getTransports(id: Int): Flow<Result<TransportsResponse>> {
        return performCall { tripApi.getTransports(id) }
    }

    suspend fun getAccommodation(id: Int): Flow<Result<MultipleAccommodationResponse>> {
        return performCall { tripApi.getAccommodation(id) }
    }

    suspend fun getPlaces(id: Int): Flow<Result<PlacesResponse>> {
        return performCall { tripApi.getPlaces(id) }
    }

    suspend fun getActivities(id: Int): Flow<Result<ActivitiesResponse>> {
        return performCall { tripApi.getActivities(id) }
    }

    suspend fun getDocuments(id: Int): Flow<Result<DocumentsMetadataResponse>> {
        return performCall { tripApi.getDocuments(id) }
    }

    suspend fun getUsers(id: Int): Flow<Result<UsersResponse>> {
        return performCall { tripApi.getUsers(id) }
    }
}