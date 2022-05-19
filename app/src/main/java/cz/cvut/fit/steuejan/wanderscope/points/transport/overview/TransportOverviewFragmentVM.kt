package cz.cvut.fit.steuejan.wanderscope.points.transport.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.response.TransportResponse
import cz.cvut.fit.steuejan.wanderscope.points.transport.repository.TransportRepository
import kotlinx.coroutines.launch

class TransportOverviewFragmentVM(transportRepository: TransportRepository) :
    AbstractPointOverviewFragmentVM<TransportResponse>(transportRepository) {

    val from = MutableLiveData<String?>()
    val to = MutableLiveData<String?>()

    override suspend fun getPointOverviewSuccess(data: TransportResponse) {
        super.getPointOverviewSuccess(data)
        viewModelScope.launch {
            from.value = data.from.address
            to.value = data.to.address
            pointOverviewLoading.value = false
        }
    }
}