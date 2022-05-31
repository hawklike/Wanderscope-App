package cz.cvut.fit.steuejan.wanderscope.points.accommodation.overview

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.extension.getNights
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent
import cz.cvut.fit.steuejan.wanderscope.app.util.callPhone
import cz.cvut.fit.steuejan.wanderscope.app.util.sendEmail
import cz.cvut.fit.steuejan.wanderscope.document.model.UploadDocumentBundle
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.response.AccommodationResponse
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.repository.AccommodationRepository
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragmentVM

class AccommodationOverviewFragmentVM(
    accommodationRepository: AccommodationRepository
) : AbstractPointOverviewFragmentVM<AccommodationResponse>(
    accommodationRepository
) {

    override val pointType = TripPointType.ACCOMMODATION

    val phone = MutableLiveData<String?>()
    val email = MutableLiveData<String?>()
    val nights = MutableLiveData<Int?>()

    val launchPhoneCall = SingleLiveEvent<Intent>()
    val sendEmail = SingleLiveEvent<Intent>()

    override suspend fun customizePointOverviewSuccess(data: AccommodationResponse) {
        phone.value = data.contact.phone
        email.value = data.contact.email
        nights.value = data.duration.getNights()?.nights
        super.website.value = data.contact.website
    }

    fun launchPhoneCall() {
        phone.value?.let {
            launchPhoneCall.value = callPhone(it)
        }
    }

    fun sendEmail() {
        email.value?.let {
            sendEmail.value = sendEmail(it)
        }
    }

    fun deleteAccommodation(tripId: Int, pointId: Int) {
        showAlertDialog(
            AlertDialogInfo(
                title = R.string.delete_accommodation_dialog_title,
                message = R.string.delete_accommodation_dialog_message,
                positiveButton = R.string.delete,
                onClickPositive = { _, _ ->
                    deletePointReady(
                        tripId,
                        pointId,
                        R.string.deleting_accommodation
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
                AccommodationOverviewFragmentDirections
                    .actionAccommodationOverviewFragmentToUploadDocumentFragment(
                        UploadDocumentBundle(tripId, pointId, TripPointType.ACCOMMODATION)
                    )
            )
        )
    }
}