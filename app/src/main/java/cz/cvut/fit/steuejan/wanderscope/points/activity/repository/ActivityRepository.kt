package cz.cvut.fit.steuejan.wanderscope.points.activity.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.ActivityApi
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.request.ActivityRequest
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.response.ActivityResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.repository.PointRepository
import kotlinx.coroutines.flow.Flow

class ActivityRepository(private val api: ActivityApi) : PointRepository<ActivityRequest, ActivityResponse> {

    override suspend fun createPoint(tripId: Int, request: ActivityRequest): Flow<Result<CreatedResponse>> {
        TODO("Not yet implemented")
    }
}