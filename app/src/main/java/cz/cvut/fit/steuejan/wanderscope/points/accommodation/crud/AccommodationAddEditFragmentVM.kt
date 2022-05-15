package cz.cvut.fit.steuejan.wanderscope.points.accommodation.crud

import androidx.lifecycle.MutableLiveData
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragmentVM

class AccommodationAddEditFragmentVM : AbstractPointAddEditFragmentVM(R.string.add_accommodation) {

    private var purpose: Purpose? = null
    private var tripId: Int? = null
    private var accommodationId: Int? = null

    val phone = MutableLiveData<String?>(null)
    val email = MutableLiveData<String?>(null)
    val website = MutableLiveData<String?>(null)

    val validateEmail = email.switchMapSuspend {
        if (it.isNullOrBlank()) OK else validator.validateEmail(it)
    }

    override val enableSubmit = super.enableSubmit.add(
        validateEmail
    )

    override suspend fun validateDates(startDate: String?, endDate: String?): Int {
        if (!shouldValidateDates) {
            return OK
        }
        if (startDate.isNullOrBlank()) {
            startDateTime = null
        }
        if (endDate.isNullOrBlank()) {
            endDateTime = null
        }
        return validator.validateDates(startDateTime?.millis, endDateTime?.millis, checkIn = true)
    }

    override fun placeFound(place: Place) {
        place.name?.let {
            name.value = it
            search.value = it
        }
        place.address?.let { address.value = it }
        place.phoneNumber?.let { phone.value = it }
        place.websiteUri?.let { website.value = it.toString() }
    }
}