package cz.cvut.fit.steuejan.wanderscope.points.accommodation.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.app.util.toUnitIfSuccess
import cz.cvut.fit.steuejan.wanderscope.document.api.DocumentApi
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.AccommodationApi
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.request.AccommodationRequest
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.response.AccommodationResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.points.common.repository.PointRepository
import kotlinx.coroutines.flow.Flow

class AccommodationRepository(private val api: AccommodationApi, documentApi: DocumentApi) :
    PointRepository<AccommodationRequest, AccommodationResponse>(documentApi) {

    override val pointType = TripPointType.ACCOMMODATION

    override suspend fun createPoint(tripId: Int, request: AccommodationRequest): Flow<Result<CreatedResponse>> {
        return performCall { api.createAccommodation(tripId, request) }
    }

    override suspend fun getPoint(tripId: Int, pointId: Int): Flow<Result<AccommodationResponse>> {
        return performCall { api.getAccommodation(tripId, pointId) }
    }

    override suspend fun editPoint(tripId: Int, pointId: Int, request: AccommodationRequest): Flow<Result<Unit>> {
        return performCall { api.editAccommodation(tripId, pointId, request) }.toUnitIfSuccess()
    }
}