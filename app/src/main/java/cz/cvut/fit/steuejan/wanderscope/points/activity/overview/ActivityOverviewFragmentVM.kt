package cz.cvut.fit.steuejan.wanderscope.points.activity.overview

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.util.goToWebsite
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.response.ActivityResponse
import cz.cvut.fit.steuejan.wanderscope.points.activity.repository.ActivityRepository
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragmentVM

class ActivityOverviewFragmentVM(activityRepository: ActivityRepository) :
    AbstractPointOverviewFragmentVM<ActivityResponse>(activityRepository) {

    val mapLink = MutableLiveData<String?>()

    val launchMapLink = SingleLiveEvent<Intent>()

    override suspend fun customizePointOverviewSuccess(data: ActivityResponse) {
        mapLink.value = data.mapLink
        super.website.value = data.website
    }

    override fun launchMapFromLabel() {
        launchMapLink() ?: super.launchMapFromLabel()
    }

    fun launchMapLink(): Unit? {
        return mapLink.value?.let {
            launchMapLink.value = goToWebsite(it)
        }
    }
}