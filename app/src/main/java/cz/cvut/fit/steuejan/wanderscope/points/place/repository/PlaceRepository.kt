package cz.cvut.fit.steuejan.wanderscope.points.place.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.points.common.repository.PointRepository
import cz.cvut.fit.steuejan.wanderscope.points.place.api.PlaceApi
import cz.cvut.fit.steuejan.wanderscope.points.place.api.request.PlaceRequest
import cz.cvut.fit.steuejan.wanderscope.points.place.api.response.PlaceResponse
import kotlinx.coroutines.flow.Flow

class PlaceRepository(private val api: PlaceApi) :
    PointRepository<PlaceRequest, PlaceResponse> {

    override suspend fun createPoint(tripId: Int, request: PlaceRequest): Flow<Result<CreatedResponse>> {
        return performCall { api.createPlace(tripId, request) }
    }

    suspend fun createPoint(
        tripId: Int,
        request: PlaceRequest,
        title: String?
    ): Flow<Result<CreatedResponse>> {
        return if (title == null) {
            createPoint(tripId, request)
        } else {
            performCall { api.createPlace(tripId, title, request) }
        }
    }
}