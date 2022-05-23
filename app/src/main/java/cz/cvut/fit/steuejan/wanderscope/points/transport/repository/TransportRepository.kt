package cz.cvut.fit.steuejan.wanderscope.points.transport.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.app.util.toUnitIfSuccess
import cz.cvut.fit.steuejan.wanderscope.document.api.DocumentApi
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.points.common.repository.PointRepository
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.TransportApi
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.request.TransportRequest
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.response.TransportResponse
import kotlinx.coroutines.flow.Flow

class TransportRepository(private val api: TransportApi, documentApi: DocumentApi) :
    PointRepository<TransportRequest, TransportResponse>(documentApi) {

    override val pointType = TripPointType.TRANSPORT

    override suspend fun createPoint(tripId: Int, request: TransportRequest): Flow<Result<CreatedResponse>> {
        return performCall { api.createTransport(tripId, request) }
    }

    override suspend fun getPoint(tripId: Int, pointId: Int): Flow<Result<TransportResponse>> {
        return performCall { api.getTransport(tripId, pointId) }
    }

    override suspend fun editPoint(tripId: Int, pointId: Int, request: TransportRequest): Flow<Result<Unit>> {
        return performCall { api.editTransport(tripId, pointId, request) }.toUnitIfSuccess()
    }
}