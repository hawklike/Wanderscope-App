package cz.cvut.fit.steuejan.wanderscope.points.transport.overview

import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.app.livedata.mediator.TripleMediatorLiveData
import cz.cvut.fit.steuejan.wanderscope.app.util.showDirections
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.response.TransportResponse
import cz.cvut.fit.steuejan.wanderscope.points.transport.repository.TransportRepository

class TransportOverviewFragmentVM(transportRepository: TransportRepository) :
    AbstractPointOverviewFragmentVM<TransportResponse>(transportRepository) {

    val from = MutableLiveData<String?>()
    val to = MutableLiveData<String?>()
    val cars = MutableLiveData<List<ChipInfo>?>()
    val seats = MutableLiveData<List<ChipInfo>?>()

    val fromCoordinatesReady = MutableLiveData<LocationBundle>()
    val toCoordinatesReady = MutableLiveData<LocationBundle>()

    val mapAndAllCoordinatesReady = TripleMediatorLiveData(
        mapReady,
        fromCoordinatesReady,
        toCoordinatesReady
    )

    override suspend fun customizePointOverviewSuccess(data: TransportResponse) {
        from.value = data.address.name
        to.value = data.to.name
        cars.value = prepareCarChips(data)
        seats.value = prepareSeatChips(data)
    }

    override fun launchMapFromLabel() {
        to.value?.let {
            val type = pointOverview.value?.type
            goToWebsite.value = showDirections(from.value, it, type)
        } ?: showToast(ToastInfo(R.string.set_a_destination))
    }

    override fun showMap(data: TransportResponse) {
        val fromCoordinates = data.coordinates.toLatLng()
        val toCoordinates = data.toCoordinates.toLatLng()
        if (fromCoordinates == null && toCoordinates == null) {
            return
        }
        showMap.value = true
        fromCoordinatesReady.value = LocationBundle(data.coordinates, data.address.name)
        toCoordinatesReady.value = LocationBundle(data.toCoordinates, data.to.name)
    }

    private suspend fun prepareCarChips(data: TransportResponse): List<ChipInfo>? {
        return withDefault {
            data.cars?.map {
                ChipInfo(it, false, textColor = R.color.colorPrimary)
            }
        }
    }

    private suspend fun prepareSeatChips(data: TransportResponse): List<ChipInfo>? {
        return withDefault {
            data.seats?.map {
                ChipInfo(it, false, textColor = R.color.colorPrimary)
            }
        }
    }
}