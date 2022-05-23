package cz.cvut.fit.steuejan.wanderscope.points.place.crud

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Contact
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Coordinates
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.getOrNullIfBlank
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.app.util.runOrNull
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.place.api.request.PlaceRequest
import cz.cvut.fit.steuejan.wanderscope.points.place.api.response.PlaceResponse
import cz.cvut.fit.steuejan.wanderscope.points.place.model.PlaceType
import cz.cvut.fit.steuejan.wanderscope.points.place.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow

class PlaceAddEditFragmentVM(
    private val repository: PlaceRepository,
    savedStateHandle: SavedStateHandle
) : AbstractPointAddEditFragmentVM<PlaceRequest, PlaceResponse>(
    repository,
    savedStateHandle
) {

    val website = MutableLiveData<String?>()
    val latitude = MutableLiveData<String?>()
    val longitude = MutableLiveData<String?>()

    override fun placeFound(place: Place) {
        super.placeFound(place)
        place.name?.let { name.value = it }
        place.address?.let { address.value = it }
        place.websiteUri?.let { website.value = it.toString() }
        place.latLng?.let {
            latitude.value = it.latitude.toString()
            longitude.value = it.longitude.toString()
        }
    }

    override suspend fun createPoint(request: PlaceRequest): Flow<Result<CreatedResponse>>? {
        return repository.createPoint(tripId ?: return null, request, placeName)
    }

    override fun createRequest(): PlaceRequest? {
        val name = name.value ?: return null
        return PlaceRequest(
            name = name,
            duration = Duration(startDateTime, endDateTime),
            type = getTypeFromSelectedItem(),
            address = Address(placeId, address.value.getOrNullIfBlank()),
            contact = Contact(website = website.value.getOrNullIfBlank()),
            coordinates = Coordinates(longitude.value.getOrNullIfBlank(), latitude.value.getOrNullIfBlank()),
            description = description.value.getOrNullIfBlank(),
            imageUrl = null
        )
    }

    private fun getTypeFromSelectedItem(): PlaceType {
        return runOrNull {
            PlaceType.values()[selectedTypePosition ?: -1]
        } ?: PlaceType.OTHER
    }
}