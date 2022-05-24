package cz.cvut.fit.steuejan.wanderscope.points.place.overview

import android.content.Intent
import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.util.multipleLet
import cz.cvut.fit.steuejan.wanderscope.app.util.showMap
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.place.api.response.PlaceResponse
import cz.cvut.fit.steuejan.wanderscope.points.place.repository.PlaceRepository

class PlaceOverviewFragmentVM(placeRepository: PlaceRepository) :
    AbstractPointOverviewFragmentVM<PlaceResponse>(placeRepository) {

    val latitude = MutableLiveData<String?>()
    val longitude = MutableLiveData<String?>()
    val wikipedia = MutableLiveData<String?>()

    val launchMapLatLon = SingleLiveEvent<Intent>()

    override suspend fun customizePointOverviewSuccess(data: PlaceResponse) {
        latitude.value = data.coordinates.latitude
        longitude.value = data.coordinates.longitude
        super.website.value = data.contact.website
        setWikipedia(data)
    }

    private fun setWikipedia(data: PlaceResponse) {
        when (Resources.getSystem().configuration.locales.get(0).language) {
            "en" -> data.wikiBrief
            "cs" -> data.wikiBriefCzech
            else -> null
        }.also { wikipedia.value = it }
    }

    fun launchMapLatLon() {
        multipleLet(latitude.value, longitude.value) { lat, lon ->
            launchMapLatLon.value = showMap(lat, lon)
        }
    }

    fun deletePlace(tripId: Int, pointId: Int) {
        showAlertDialog(
            AlertDialogInfo(
                title = R.string.delete_place_dialog_title,
                message = R.string.delete_place_dialog_message,
                positiveButton = R.string.delete,
                onClickPositive = { _, _ ->
                    deletePointReady(
                        tripId,
                        pointId,
                        R.string.deleting_place
                    )
                }
            )
        )
    }
}