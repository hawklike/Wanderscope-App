package cz.cvut.fit.steuejan.wanderscope.points.place.overview

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.place.api.response.PlaceResponse
import cz.cvut.fit.steuejan.wanderscope.points.place.repository.PlaceRepository

class PlaceOverviewFragmentVM(placeRepository: PlaceRepository) :
    AbstractPointOverviewFragmentVM<PlaceResponse>(placeRepository) {

    val latitude = MutableLiveData<String?>()
    val longitude = MutableLiveData<String?>()
    val wikipedia = MutableLiveData<String?>()

    override suspend fun customizePointOverviewSuccess(data: PlaceResponse) {
        latitude.value = data.coordinates.latitude
        longitude.value = data.coordinates.longitude
        super.website.value = data.contact.website
        setWikipedia(data)
    }

    private fun setWikipedia(data: PlaceResponse) {
        when (Resources.getSystem().configuration.locales.get(0).language) {
            "en" -> data.wikiBrief
            "cs" -> data.wikiBrief
            else -> null
        }.also { wikipedia.value = it }
    }
}