package cz.cvut.fit.steuejan.wanderscope.points.common.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.api.request.PointRequest
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import kotlinx.coroutines.flow.Flow

interface PointRepository<in Request : PointRequest, out Response : PointResponse> {
    suspend fun createPoint(tripId: Int, request: Request): Flow<Result<CreatedResponse>>
}