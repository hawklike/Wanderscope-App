package cz.cvut.fit.steuejan.wanderscope.points.activity.crud

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.getOrNullIfBlank
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.request.ActivityRequest
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.response.ActivityResponse
import cz.cvut.fit.steuejan.wanderscope.points.activity.model.ActivityType
import cz.cvut.fit.steuejan.wanderscope.points.activity.repository.ActivityRepository
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragmentVM
import kotlinx.coroutines.launch

class ActivityAddEditFragmentVM(
    repository: ActivityRepository,
    savedStateHandle: SavedStateHandle
) : AbstractPointAddEditFragmentVM<ActivityRequest, ActivityResponse>(
    repository,
    savedStateHandle
) {

    val website = MutableLiveData<String?>()
    val mapLink = MutableLiveData<String?>()

    override fun setupEdit(point: ActivityResponse, title: Int) {
        super.setupEdit(point, title)
        viewModelScope.launch {
            website.value = point.website
            mapLink.value = point.mapLink
            type.value = point.type.toStringRes()
        }
    }

    override fun placeFound(place: Place) {
        super.placeFound(place)
        place.name?.let { name.value = it }
        place.address?.let { address.value = it }
        place.websiteUri?.let { website.value = it.toString() }
    }

    override fun createRequest(): ActivityRequest? {
        val name = name.value ?: return null
        return ActivityRequest(
            name = name,
            duration = Duration(startDateTime, endDateTime),
            type = getTypeFromSelectedItem(),
            address = Address(placeId, address.value.getOrNullIfBlank()),
            mapLink = mapLink.value.getOrNullIfBlank(),
            description = description.value.getOrNullIfBlank(),
            website = website.value.getOrNullIfBlank(),
            coordinates = coordinates
        )
    }

    private fun getTypeFromSelectedItem(): ActivityType {
        return ActivityType.values().getOrNull(selectedTypePosition ?: -1)
            ?: ActivityType.OTHER
    }
}