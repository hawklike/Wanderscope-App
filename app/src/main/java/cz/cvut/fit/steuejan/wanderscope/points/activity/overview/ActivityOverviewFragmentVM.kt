package cz.cvut.fit.steuejan.wanderscope.points.activity.overview

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent
import cz.cvut.fit.steuejan.wanderscope.app.util.goToWebsite
import cz.cvut.fit.steuejan.wanderscope.document.model.UploadDocumentBundle
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.response.ActivityResponse
import cz.cvut.fit.steuejan.wanderscope.points.activity.repository.ActivityRepository
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragmentVM

class ActivityOverviewFragmentVM(
    activityRepository: ActivityRepository
) : AbstractPointOverviewFragmentVM<ActivityResponse>(
    activityRepository
) {

    override val pointType = TripPointType.ACTIVITY

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

    fun deleteActivity(tripId: Int, pointId: Int) {
        showAlertDialog(
            AlertDialogInfo(
                title = R.string.delete_activity_dialog_title,
                message = R.string.delete_activity_dialog_message,
                positiveButton = R.string.delete,
                onClickPositive = { _, _ ->
                    deletePointReady(
                        tripId,
                        pointId,
                        R.string.deleting_activity
                    )
                }
            )
        )
    }

    fun addDocument() {
        val tripId = pointOverview.value?.tripId ?: return
        val pointId = pointOverview.value?.id ?: return
        navigateTo(
            NavigationEvent.Action(
                ActivityOverviewFragmentDirections
                    .actionActivityOverviewFragmentToUploadDocumentFragment(
                        UploadDocumentBundle(tripId, pointId, TripPointType.ACTIVITY)
                    )
            )
        )
    }
}