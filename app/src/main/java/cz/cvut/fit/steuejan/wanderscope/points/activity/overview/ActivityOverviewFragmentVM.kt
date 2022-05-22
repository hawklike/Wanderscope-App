package cz.cvut.fit.steuejan.wanderscope.points.activity.overview

import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.response.ActivityResponse
import cz.cvut.fit.steuejan.wanderscope.points.activity.repository.ActivityRepository
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragmentVM

class ActivityOverviewFragmentVM(activityRepository: ActivityRepository) :
    AbstractPointOverviewFragmentVM<ActivityResponse>(activityRepository) {

    val mapLink = MutableLiveData<String?>()

    override suspend fun customizePointOverviewSuccess(data: ActivityResponse) {
        mapLink.value = data.mapLink
        super.website.value = data.website
    }
}