package cz.cvut.fit.steuejan.wanderscope.points.place.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.document.api.DocumentApi
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.points.common.repository.PointRepository
import cz.cvut.fit.steuejan.wanderscope.points.place.api.PlaceApi
import cz.cvut.fit.steuejan.wanderscope.points.place.api.request.PlaceRequest
import cz.cvut.fit.steuejan.wanderscope.points.place.api.response.PlaceResponse
import kotlinx.coroutines.flow.Flow

class PlaceRepository(private val api: PlaceApi, documentApi: DocumentApi) :
    PointRepository<PlaceRequest, PlaceResponse>(documentApi) {

    override val pointType = TripPointType.PLACE

    override suspend fun createPoint(tripId: Int, request: PlaceRequest): Flow<Result<CreatedResponse>> {
        return performCall { api.createPlace(tripId, request, null) }
    }

    suspend fun createPoint(tripId: Int, request: PlaceRequest, title: String?) = performCall {
        api.createPlace(tripId, request, title)
    }

    override suspend fun getPoint(tripId: Int, pointId: Int): Flow<Result<PlaceResponse>> {
        return performCall { api.getPlace(tripId, pointId) }
    }
}