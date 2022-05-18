package cz.cvut.fit.steuejan.wanderscope.points.transport.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.points.common.repository.PointRepository
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.TransportApi
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.request.TransportRequest
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.response.TransportResponse
import kotlinx.coroutines.flow.Flow

class TransportRepository(private val api: TransportApi) : PointRepository<TransportRequest, TransportResponse> {

    override suspend fun createPoint(tripId: Int, request: TransportRequest): Flow<Result<CreatedResponse>> {
        return performCall { api.createTransport(tripId, request) }
    }
}