package cz.cvut.fit.steuejan.wanderscope.points.accommodation.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.AccommodationApi
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.request.AccommodationRequest
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.response.AccommodationResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.repository.PointRepository
import kotlinx.coroutines.flow.Flow

class AccommodationRepository(private val api: AccommodationApi) :
    PointRepository<AccommodationRequest, AccommodationResponse> {

    override suspend fun createPoint(tripId: Int, request: AccommodationRequest): Flow<Result<CreatedResponse>> {
        return performCall { api.createAccommodation(tripId, request) }
    }
}