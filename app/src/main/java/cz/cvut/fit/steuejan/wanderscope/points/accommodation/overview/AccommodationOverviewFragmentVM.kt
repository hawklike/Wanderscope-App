package cz.cvut.fit.steuejan.wanderscope.points.accommodation.overview

import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.extension.getNights
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.response.AccommodationResponse
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.repository.AccommodationRepository
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragmentVM

class AccommodationOverviewFragmentVM(accommodationRepository: AccommodationRepository) :
    AbstractPointOverviewFragmentVM<AccommodationResponse>(accommodationRepository) {

    val phone = MutableLiveData<String?>()
    val email = MutableLiveData<String?>()
    val nights = MutableLiveData<Int?>()

    override suspend fun customizePointOverviewSuccess(data: AccommodationResponse) {
        phone.value = data.contact.phone
        email.value = data.contact.email
        nights.value = data.duration.getNights()
        super.website.value = data.contact.website
    }
}