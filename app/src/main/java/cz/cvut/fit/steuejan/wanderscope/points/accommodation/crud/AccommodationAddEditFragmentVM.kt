package cz.cvut.fit.steuejan.wanderscope.points.accommodation.crud

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Contact
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.getOrNullIfBlank
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.util.runOrNull
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.request.AccommodationRequest
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.response.AccommodationResponse
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.model.AccommodationType
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.repository.AccommodationRepository
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragmentVM
import kotlinx.coroutines.launch

class AccommodationAddEditFragmentVM(
    repository: AccommodationRepository,
    savedStateHandle: SavedStateHandle
) : AbstractPointAddEditFragmentVM<AccommodationRequest, AccommodationResponse>(
    repository,
    savedStateHandle
) {

    val phone = MutableLiveData<String?>()
    val website = MutableLiveData<String?>()
    val email = MutableLiveData<String?>(null)
    val type = MutableLiveData<String>()

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
        super.placeFound(place)
        place.name?.let {
            name.value = it
            search.value = it
        }
        place.address?.let { address.value = it }
        place.phoneNumber?.let { phone.value = it }
        place.websiteUri?.let { website.value = it.toString() }
    }

    fun submit() {
        viewModelScope.launch {
            val name = name.value ?: return@launch
            submitLoading.value = true

            val type = runOrNull {
                type.value?.let { AccommodationType.valueOf(it.uppercase()) }
            } ?: AccommodationType.OTHER

            val request = AccommodationRequest(
                name = name,
                duration = Duration(startDateTime, endDateTime),
                type = type,
                address = Address(getStateData(PLACE_ID), address.value.getOrNullIfBlank()),
                contact = Contact(
                    phone.value.getOrNullIfBlank(),
                    email.value.getOrNullIfBlank(),
                    website.value.getOrNullIfBlank()
                ),
                description = description.value.getOrNullIfBlank()
            )
            submit(request)
        }
    }
}