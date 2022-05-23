package cz.cvut.fit.steuejan.wanderscope.points.common.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.document.api.DocumentApi
import cz.cvut.fit.steuejan.wanderscope.document.response.DocumentsMetadataResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.points.common.api.request.PointRequest
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import kotlinx.coroutines.flow.Flow

abstract class PointRepository<in Request : PointRequest, out Response : PointResponse>(
    protected val documentApi: DocumentApi
) {

    abstract val pointType: TripPointType

    abstract suspend fun createPoint(tripId: Int, request: Request): Flow<Result<CreatedResponse>>
    abstract suspend fun getPoint(tripId: Int, pointId: Int): Flow<Result<Response>>
    abstract suspend fun editPoint(tripId: Int, pointId: Int, request: Request): Flow<Result<Unit>>

    suspend fun getDocuments(tripId: Int, pointId: Int): Flow<Result<DocumentsMetadataResponse>> {
        return performCall { documentApi.getPointDocuments(tripId, pointType.toString(), pointId) }
    }
}