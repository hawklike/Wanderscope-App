package cz.cvut.fit.steuejan.wanderscope.points.common.overview

import android.content.Intent
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.LoadingMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Coordinates
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.EmptyItem
import cz.cvut.fit.steuejan.wanderscope.app.extension.*
import cz.cvut.fit.steuejan.wanderscope.app.livedata.AnySingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.livedata.mediator.PairMediatorLiveData
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.app.util.goToWebsite
import cz.cvut.fit.steuejan.wanderscope.app.util.model.DaysHoursMinutes
import cz.cvut.fit.steuejan.wanderscope.app.util.showMap
import cz.cvut.fit.steuejan.wanderscope.document.response.DocumentsMetadataResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.repository.PointRepository
import kotlinx.coroutines.CoroutineScope

abstract class AbstractPointOverviewFragmentVM<Response : PointResponse>(
    protected val pointRepository: PointRepository<*, Response>
) : BaseViewModel() {

    val startDate = MutableLiveData<String?>()
    val endDate = MutableLiveData<String?>()
    val type = MutableLiveData<Int>()
    val icon = MutableLiveData<Int>()
    val description = MutableLiveData<String?>()
    val address = MutableLiveData<String?>()
    val duration = MutableLiveData<DaysHoursMinutes?>()

    val documents = MutableLiveData<List<RecyclerItem>>()

    val pointOverview = MutableLiveData<Response>()

    protected val pointOverviewLoading = MutableLiveData<Boolean>()
    protected val documentsLoading = MutableLiveData<Boolean>()

    val mapReady = MutableLiveData<GoogleMap>()
    val coordinatesReady = MutableLiveData<LocationBundle>()
    val showMap = MutableLiveData<Boolean>()

    val mapAndCoordinatesReady = PairMediatorLiveData(mapReady, coordinatesReady)

    val loading = LoadingMediator(
        pointOverviewLoading,
        documentsLoading
    ).delayAndReturn(200) //smooth loading

    val website = MutableLiveData<String?>()

    val goToWebsite = SingleLiveEvent<Intent>()
    val launchMap = SingleLiveEvent<Intent>()

    val deleteIsSuccess = AnySingleLiveEvent()
    val deleteLoading = SingleLiveEvent<Int>()

    fun getPoint(tripId: Int, pointId: Int) {
        viewModelScope.launchIO { getPointOverview(tripId, pointId, this) }
        viewModelScope.launchIO { getDocuments(tripId, pointId, this) }
    }

    protected open suspend fun getPointOverview(tripId: Int, pointId: Int, scope: CoroutineScope) {
        pointRepository.getPoint(tripId, pointId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, pointOverviewLoading)
                is Result.Loading -> pointOverviewLoading.value = true
                is Result.Success -> getPointOverviewSuccess(it.data)
            }
        }
    }

    protected open suspend fun getPointOverviewSuccess(data: Response) {
        pointOverview.value = data
        startDate.value = data.duration.startDate?.toNiceString()
        endDate.value = data.duration.endDate?.toNiceString()
        type.value = data.type.toStringRes()
        icon.value = data.type.toIcon()
        description.value = data.description
        address.value = data.address.name
        duration.value = data.duration.getDaysHoursAndMinutes()
        showMap(data)
        customizePointOverviewSuccess(data)
        pointOverviewLoading.value = false
    }

    protected open fun showMap(data: Response) {
        data.coordinates.toLatLng() ?: return
        showMap.value = true
        coordinatesReady.value = LocationBundle(data.coordinates, data.address.name)
    }

    abstract suspend fun customizePointOverviewSuccess(data: Response)

    protected open suspend fun getDocuments(tripId: Int, pointId: Int, scope: CoroutineScope) {
        pointRepository.getDocuments(tripId, pointId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, documentsLoading)
                is Result.Loading -> documentsLoading.value = true
                is Result.Success -> documentsSuccess(it.data)
            }
        }
    }

    protected suspend fun documentsSuccess(data: DocumentsMetadataResponse) {
        val items = data.documents.map { it.toOverviewItem() }
        showUpdateToast(items, documents.value, R.string.documents_updated)
        documents.value = items.ifEmpty { listOf(EmptyItem.documents()) }
        documentsLoading.value = false
    }

    protected fun failure(error: Error, loading: MutableLiveData<Boolean>) {
        loading.value = false
        unexpectedError(error)
    }

    fun goToWebsite() {
        website.value?.let {
            goToWebsite.value = goToWebsite(it)
        }
    }

    fun launchMap() {
        address.value?.let {
            launchMap.value = showMap(it)
        }
    }

    open fun launchMapFromLabel() {
        pointOverview.value?.name?.let {
            launchMap.value = showMap(it)
        }
    }

    protected fun deletePointReady(tripId: Int, pointId: Int, @StringRes loadingTitle: Int) {
        viewModelScope.launchIO {
            pointRepository.deletePoint(tripId, pointId).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> unexpectedError(it.error)
                    is Result.Loading -> deleteLoading.value = loadingTitle
                    is Result.Success -> deleteIsSuccess.publish()

                }
            }
        }
    }

    data class LocationBundle(
        val coordinates: Coordinates?,
        val title: String?
    )
}