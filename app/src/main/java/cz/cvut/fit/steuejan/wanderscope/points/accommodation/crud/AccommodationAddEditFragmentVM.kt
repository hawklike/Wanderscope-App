package cz.cvut.fit.steuejan.wanderscope.points.accommodation.crud

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.ValidateDates
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Contact
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.getOrNullIfBlank
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.util.runOrNull
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.request.AccommodationRequest
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.model.AccommodationType
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.repository.AccommodationRepository
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragmentVM

class AccommodationAddEditFragmentVM(
    repository: AccommodationRepository,
    savedStateHandle: SavedStateHandle
) : AbstractPointAddEditFragmentVM<AccommodationRequest>(
    repository,
    savedStateHandle
) {

    val phone = MutableLiveData<String?>()
    val website = MutableLiveData<String?>()
    val email = MutableLiveData<String?>(null)

    val validateEmail = email.switchMapSuspend {
        if (it.isNullOrBlank()) OK else validator.validateEmail(it)
    }

    override val enableSubmit = super.enableSubmit.add(
        validateEmail
    )

    override suspend fun validateDates(startDate: String?, endDate: String?, type: ValidateDates): Int {
        return super.validateDates(startDate, endDate, ValidateDates.CHECKIN)
    }

    override fun placeFound(place: Place) {
        super.placeFound(place)
        place.name?.let { name.value = it }
        place.address?.let { address.value = it }
        place.phoneNumber?.let { phone.value = it }
        place.websiteUri?.let { website.value = it.toString() }
    }

    override fun createRequest(): AccommodationRequest? {
        val name = name.value ?: return null
        return AccommodationRequest(
            name = name,
            duration = Duration(startDateTime, endDateTime),
            type = getTypeFromSelectedItem(),
            address = Address(placeId, address.value.getOrNullIfBlank()),
            contact = Contact(
                phone.value.getOrNullIfBlank(),
                email.value.getOrNullIfBlank(),
                website.value.getOrNullIfBlank()
            ),
            description = description.value.getOrNullIfBlank(),
            coordinates = coordinates
        )
    }

    private fun getTypeFromSelectedItem(): AccommodationType {
        return runOrNull {
            AccommodationType.values()[selectedTypePosition ?: -1]
        } ?: AccommodationType.OTHER
    }
}