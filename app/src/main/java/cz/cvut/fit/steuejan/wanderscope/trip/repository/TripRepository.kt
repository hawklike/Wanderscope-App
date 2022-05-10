package cz.cvut.fit.steuejan.wanderscope.trip.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.trip.api.TripApi
import cz.cvut.fit.steuejan.wanderscope.trip.api.request.TripRequest
import kotlinx.coroutines.flow.Flow

class TripRepository(private val tripApi: TripApi) {

    suspend fun createTrip(request: TripRequest): Flow<Result<CreatedResponse>> {
        return performCall { tripApi.createTrip(request) }
    }
}