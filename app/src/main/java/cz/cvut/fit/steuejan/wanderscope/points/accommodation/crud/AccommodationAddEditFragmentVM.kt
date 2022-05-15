package cz.cvut.fit.steuejan.wanderscope.points.accommodation.crud

import androidx.lifecycle.MutableLiveData
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent

class AccommodationAddEditFragmentVM : BaseViewModel() {

    val title = MutableLiveData<Int>()

    private var purpose: Purpose? = null
    private var tripId: Int? = null
    private var accommodationId: Int? = null

    val findAccommodationEvent = SingleLiveEvent<String?>()

    val search = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val address = MutableLiveData<String?>(null)
    val startDate = MutableLiveData<String?>(null)
    val endDate = MutableLiveData<String?>(null)
    val phone = MutableLiveData<String?>(null)
    val email = MutableLiveData<String?>(null)
    val website = MutableLiveData<String?>(null)
    val description = MutableLiveData<String?>()

    val validateName = name.switchMapSuspend {
        validator.validateIfNotEmpty(it)
    }

    val validateAddress = address.switchMapSuspend {
        it ?: return@switchMapSuspend OK
        validator.validateIfNotTooLong(it, Constants.OTHER_MAX_LEN)
    }

    val validateEmail = email.switchMapSuspend {
        if (it.isNullOrBlank()) {
            return@switchMapSuspend OK
        }
        validator.validateEmail(it)
    }

    val enableSubmit = ValidationMediator(
        validateName,
        validateAddress,
        validateEmail
    )

    init {
        title.value = R.string.add_accommodation
        purpose = Purpose.CREATE
    }

    fun findAccommodation() {
        findAccommodationEvent.value = search.value
    }

    fun placeFound(place: Place) {
        place.name?.let {
            name.value = it
            search.value = it
        }
        place.address?.let { address.value = it }
        place.phoneNumber?.let { phone.value = it }
        place.websiteUri?.let { website.value = it.toString() }
    }

    enum class Purpose {
        CREATE, EDIT
    }
}