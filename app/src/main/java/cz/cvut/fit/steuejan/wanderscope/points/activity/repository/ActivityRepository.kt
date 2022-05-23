package cz.cvut.fit.steuejan.wanderscope.points.activity.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.app.util.toUnitIfSuccess
import cz.cvut.fit.steuejan.wanderscope.document.api.DocumentApi
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.ActivityApi
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.request.ActivityRequest
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.response.ActivityResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.points.common.repository.PointRepository
import kotlinx.coroutines.flow.Flow

class ActivityRepository(private val api: ActivityApi, documentApi: DocumentApi) :
    PointRepository<ActivityRequest, ActivityResponse>(documentApi) {

    override val pointType = TripPointType.ACTIVITY

    override suspend fun createPoint(tripId: Int, request: ActivityRequest): Flow<Result<CreatedResponse>> {
        return performCall { api.createActivity(tripId, request) }
    }

    override suspend fun getPoint(tripId: Int, pointId: Int): Flow<Result<ActivityResponse>> {
        return performCall { api.getActivity(tripId, pointId) }
    }

    override suspend fun editPoint(tripId: Int, pointId: Int, request: ActivityRequest): Flow<Result<Unit>> {
        return performCall { api.editActivity(tripId, pointId, request) }.toUnitIfSuccess()
    }

    override suspend fun deletePoint(tripId: Int, pointId: Int): Flow<Result<Unit>> {
        return performCall { api.deleteActivity(tripId, pointId) }.toUnitIfSuccess()
    }
}