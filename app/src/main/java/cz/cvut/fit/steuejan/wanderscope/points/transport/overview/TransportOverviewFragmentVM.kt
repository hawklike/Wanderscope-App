package cz.cvut.fit.steuejan.wanderscope.points.transport.overview

import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.response.TransportResponse
import cz.cvut.fit.steuejan.wanderscope.points.transport.repository.TransportRepository

class TransportOverviewFragmentVM(transportRepository: TransportRepository) :
    AbstractPointOverviewFragmentVM<TransportResponse>(transportRepository) {

    val from = MutableLiveData<String?>()
    val to = MutableLiveData<String?>()
    val cars = MutableLiveData<List<ChipInfo>?>()
    val seats = MutableLiveData<List<ChipInfo>?>()

    override suspend fun customizePointOverviewSuccess(data: TransportResponse) {
        from.value = data.from.address
        to.value = data.to.address
        cars.value = prepareCarChips(data)
        seats.value = prepareSeatChips(data)
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