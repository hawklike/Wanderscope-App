package cz.cvut.fit.steuejan.wanderscope.trip.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.LoadingMediator
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.data.DocumentType
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.common.map.LatLngBundle
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.DurationString
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.EmptyItem
import cz.cvut.fit.steuejan.wanderscope.app.extension.*
import cz.cvut.fit.steuejan.wanderscope.app.livedata.AnySingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent.Action
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Status
import cz.cvut.fit.steuejan.wanderscope.app.session.SessionManager
import cz.cvut.fit.steuejan.wanderscope.document.api.response.DocumentsMetadataResponse
import cz.cvut.fit.steuejan.wanderscope.document.model.DownloadedFile
import cz.cvut.fit.steuejan.wanderscope.document.model.UploadDocumentBundle
import cz.cvut.fit.steuejan.wanderscope.document.repository.DocumentRepository
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.response.MultipleAccommodationResponse
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.response.ActivitiesResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import cz.cvut.fit.steuejan.wanderscope.points.place.api.response.PlacesResponse
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.response.TransportResponse
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.response.TransportsResponse
import cz.cvut.fit.steuejan.wanderscope.trip.api.response.TripResponse
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentDirections
import cz.cvut.fit.steuejan.wanderscope.trip.repository.TripRepository
import cz.cvut.fit.steuejan.wanderscope.user.api.response.UsersResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class TripOverviewFragmentVM(
    private val tripRepository: TripRepository,
    private val documentRepository: DocumentRepository,
    private val sessionManager: SessionManager
) : BaseViewModel() {

    val title = MutableLiveData<String>()
    val duration = MutableLiveData<DurationString>()
    val description = MutableLiveData<String?>()

    val transport = MutableLiveData<List<RecyclerItem>>()
    val accommodation = MutableLiveData<List<RecyclerItem>>()
    val places = MutableLiveData<List<RecyclerItem>>()
    val activities = MutableLiveData<List<RecyclerItem>>()
    val documents = MutableLiveData<List<RecyclerItem>>()
    val travellers = MutableLiveData<List<RecyclerItem>>()

    val transportCoordinates = MutableLiveData<List<LatLngBundle>>()
    val accommodationCoordinates = MutableLiveData<List<LatLngBundle>>()
    val placeCoordinates = MutableLiveData<List<LatLngBundle>>()
    val activityCoordinates = MutableLiveData<List<LatLngBundle>>()

    val allCoordinates = SingleLiveEvent<List<LatLngBundle>>()

    val tripOverview = MutableLiveData<TripResponse>()

    val documentSuccessRequest = AnySingleLiveEvent()

    private val tripOverviewLoading = MutableLiveData<Boolean>()
    private val accommodationLoading = MutableLiveData<Boolean>()
    private val transportLoading = MutableLiveData<Boolean>()
    private val placesLoading = MutableLiveData<Boolean>()
    private val activitiesLoading = MutableLiveData<Boolean>()
    private val documentsLoading = MutableLiveData<Boolean>()
    private val travellersLoading = MutableLiveData<Boolean>()

    val documentActionLoading = MutableLiveData<Boolean>()

    val loading = LoadingMediator(
        tripOverviewLoading,
        accommodationLoading,
        transportLoading,
        placesLoading,
        activitiesLoading,
        documentsLoading,
        travellersLoading
    ).delayAndReturn(Constants.DELAY_LOADING) //loading is smoother

    val leaveTripLoading = AnySingleLiveEvent()
    val leaveTripSuccess = AnySingleLiveEvent()
    val deleteTripLoading = AnySingleLiveEvent()
    val deleteTripSuccess = AnySingleLiveEvent()

    fun getTrip(tripId: Int, whatToLoad: Load) {
        when (whatToLoad) {
            Load.ALL -> loadAll(tripId)
            Load.TRANSPORT -> loadTransport(tripId)
            Load.ACCOMMODATION -> loadAccommodation(tripId)
            Load.PLACES -> loadPlaces(tripId)
            Load.ACTIVITIES -> loadActivities(tripId)
            Load.DOCUMENTS -> loadDocuments(tripId)
            Load.USERS -> loadUsers(tripId)
            Load.TRIP -> loadTripOverview(tripId)
        }
    }

    private fun loadAll(tripId: Int) {
        loadTripOverview(tripId)
        loadAccommodation(tripId)
        loadTransport(tripId)
        loadActivities(tripId)
        loadPlaces(tripId)
        loadDocuments(tripId)
        loadUsers(tripId)
    }

    private fun loadTripOverview(tripId: Int) {
        viewModelScope.launchIO { getTripOverview(tripId, this) }
    }

    private fun loadAccommodation(tripId: Int) {
        viewModelScope.launchIO { getAccommodation(tripId, this) }
    }

    private fun loadTransport(tripId: Int) {
        viewModelScope.launchIO { getTransport(tripId, this) }
    }

    private fun loadActivities(tripId: Int) {
        viewModelScope.launchIO { getActivities(tripId, this) }
    }

    private fun loadPlaces(tripId: Int) {
        viewModelScope.launchIO { getPlaces(tripId, this) }
    }

    private fun loadDocuments(tripId: Int) {
        viewModelScope.launchIO { getDocuments(tripId, this) }

    }

    private fun loadUsers(tripId: Int) {
        viewModelScope.launchIO { getUsers(tripId, this) }
    }

    private suspend fun getTripOverview(tripId: Int, scope: CoroutineScope) {
        tripRepository.getTrip(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, tripOverviewLoading)
                is Result.Loading -> tripOverviewLoading.value = true
                is Result.Success -> getTripOverviewSuccess(it.data)
            }
        }
    }

    private suspend fun getTripOverviewSuccess(data: TripResponse) {
        tripOverview.value = data
        title.value = data.name
        duration.value = data.duration.toDurationString()
        description.value = data.description
        tripOverviewLoading.value = false
    }

    private fun failure(error: Error, loading: MutableLiveData<Boolean>) {
        loading.value = false
        unexpectedError(error)
    }

    private suspend fun getAccommodation(tripId: Int, scope: CoroutineScope) {
        tripRepository.getAccommodation(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, accommodationLoading)
                is Result.Loading -> accommodationLoading.value = true
                is Result.Success -> accommodationSuccess(it.data)
            }
        }
    }

    private suspend fun accommodationSuccess(data: MultipleAccommodationResponse) {
        val items = withDefault { data.accommodation.map { it.toOverviewItem() } }
        accommodation.value = items.ifEmpty { listOf(EmptyItem.accommodation()) }
        accommodationLoading.value = false
        viewModelScope.launch {
            val coordinates = extractCoordinates(data.accommodation, TripPointType.ACCOMMODATION)
            accommodationCoordinates.value = coordinates
        }
    }

    private suspend fun getTransport(tripId: Int, scope: CoroutineScope) {
        tripRepository.getTransports(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, transportLoading)
                is Result.Loading -> transportLoading.value = true
                is Result.Success -> transportSuccess(it.data)
            }
        }
    }

    private suspend fun transportSuccess(data: TransportsResponse) {
        val items = withDefault { data.transports.map { it.toOverviewItem() } }
        transport.value = items.ifEmpty { listOf(EmptyItem.transport()) }
        transportLoading.value = false
        viewModelScope.launch {
            val fromCoordinates = extractCoordinates(data.transports, TripPointType.TRANSPORT)
            val toCoordinates = extractTransportCoordinates(data.transports, TripPointType.TRANSPORT)
            transportCoordinates.value = fromCoordinates + toCoordinates
        }
    }

    private suspend fun extractTransportCoordinates(
        transport: List<TransportResponse>,
        pointType: TripPointType
    ): List<LatLngBundle> {
        return withDefault {
            transport.fold(emptyList()) { acc, transport ->
                transport.toCoordinates.toLatLng()?.let {
                    acc + LatLngBundle(it, transport.name, pointType)
                } ?: acc
            }
        }
    }

    private suspend fun getActivities(tripId: Int, scope: CoroutineScope) {
        tripRepository.getActivities(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, activitiesLoading)
                is Result.Loading -> activitiesLoading.value = true
                is Result.Success -> activitiesSuccess(it.data)
            }
        }
    }

    private suspend fun activitiesSuccess(data: ActivitiesResponse) {
        val items = withDefault { data.activities.map { it.toOverviewItem() } }
        activities.value = items.ifEmpty { listOf(EmptyItem.activities()) }
        activitiesLoading.value = false
        viewModelScope.launch {
            val coordinates = extractCoordinates(data.activities, TripPointType.ACTIVITY)
            activityCoordinates.value = coordinates
        }
    }

    private suspend fun getPlaces(tripId: Int, scope: CoroutineScope) {
        tripRepository.getPlaces(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, placesLoading)
                is Result.Loading -> placesLoading.value = true
                is Result.Success -> placesSuccess(it.data)
            }
        }
    }

    private suspend fun placesSuccess(data: PlacesResponse) {
        val items = withDefault { data.places.map { it.toOverviewItem() } }
        places.value = items.ifEmpty { listOf(EmptyItem.places()) }
        placesLoading.value = false
        viewModelScope.launch {
            val coordinates = extractCoordinates(data.places, TripPointType.PLACE)
            placeCoordinates.value = coordinates
        }
    }

    private suspend fun extractCoordinates(
        points: List<PointResponse>,
        pointType: TripPointType
    ): List<LatLngBundle> {
        return withDefault {
            points.fold(emptyList()) { acc, point ->
                point.coordinates.toLatLng()?.let {
                    acc + LatLngBundle(it, point.name, pointType)
                } ?: acc
            }
        }
    }

    private suspend fun getDocuments(tripId: Int, scope: CoroutineScope) {
        tripRepository.getDocuments(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, documentsLoading)
                is Result.Loading -> documentsLoading.value = true
                is Result.Success -> documentsSuccess(it.data)
            }
        }
    }

    private suspend fun documentsSuccess(data: DocumentsMetadataResponse) {
        val items = withDefault { data.documents.map { it.toOverviewItem() } }
        documents.value = items.ifEmpty { listOf(EmptyItem.documents()) }
        documentsLoading.value = false
        delay(100) //update the recycler meanwhile
        documentSuccessRequest.publish()
    }

    private suspend fun getUsers(tripId: Int, scope: CoroutineScope) {
        tripRepository.getUsers(tripId).safeCollect(scope) {
            when (it) {
                is Result.Cache -> TODO()
                is Result.Failure -> failure(it.error, travellersLoading)
                is Result.Loading -> travellersLoading.value = true
                is Result.Success -> usersSuccess(it.data)
            }
        }
    }

    private suspend fun usersSuccess(data: UsersResponse) {
        val items = withDefault { data.users.map { it.toItem(false) } }
        travellers.value = items
        travellersLoading.value = false
    }

    fun deleteTrip(id: Int) {
        showAlertDialog(
            AlertDialogInfo(
                title = R.string.delete_trip_dialog_title,
                message = R.string.delete_trip_dialog_message,
                positiveButton = R.string.delete,
                onClickPositive = { _, _ -> deleteTripReady(id) }
            )
        )
    }

    private fun deleteTripReady(id: Int) {
        viewModelScope.launchIO {
            tripRepository.deleteTrip(id).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> unexpectedError(it.error)
                    is Result.Loading -> deleteTripLoading.publish()
                    is Result.Success -> deleteTripSuccess.publish()
                }
            }
        }
    }

    fun leaveTrip(tripId: Int) {
        viewModelScope.launchIO {
            tripRepository.leaveTrip(tripId).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> leaveTripFailure(it.error)
                    is Result.Loading -> leaveTripLoading.publish()
                    is Result.Success -> leaveTripSuccess.publish()
                }
            }
        }
    }

    private fun leaveTripFailure(error: Error) {
        if (error.reason?.customCode == 1) {
            showSnackbar(SnackbarInfo.error(R.string.cannot_leave_error_message))
        } else {
            unexpectedError()
        }
    }

    fun manageUsers() {
        tripOverview.value?.let {
            navigateTo(
                Action(
                    TripPagerFragmentDirections.actionTripPagerFragmentToTripUsersFragment(
                        it.id,
                        it.userRole
                    )
                )
            )
        } ?: showToast(ToastInfo(R.string.unexpected_error_short))
    }

    fun downloadDocument(documentId: Int, name: String, type: DocumentType, key: String? = null) {
        val tripId = tripOverview.value?.id ?: return
        validateKey(key) ?: return
        viewModelScope.launchIO {
            documentRepository.getDocument(tripId, documentId, key?.toBase64()).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> downloadDocumentFailure(it.error)
                    is Result.Loading -> documentActionLoading.value = true
                    is Result.Success -> downloadDocumentSuccess(it.data, documentId, name, type)
                }
            }
        }
    }

    private fun validateKey(key: String?): Unit? {
        key ?: return Unit
        val validation = validator.validateDocumentKey(key.getOrNullIfBlank() ?: " ")
        if (validation != OK) {
            showSnackbar(SnackbarInfo.error(validation))
            return null
        }
        return Unit
    }

    private fun downloadDocumentSuccess(data: ResponseBody, documentId: Int, name: String, type: DocumentType) {
        val filename = "${documentId}_$name"
        saveAndOpenFile(DownloadedFile(data.source(), filename, type))
    }

    private fun downloadDocumentFailure(error: Error) {
        documentActionLoading.value = false
        if (error.reason?.status == Status.FORBIDDEN) {
            showSnackbar(SnackbarInfo.error(R.string.document_key_wrong))
        } else {
            unexpectedError(error)
        }
    }

    fun addDocument() {
        navigateTo(
            Action(
                TripPagerFragmentDirections.actionTripPagerFragmentToUploadDocumentFragment(
                    UploadDocumentBundle(tripOverview.value?.id ?: return)
                )
            )
        )
    }

    fun deleteDocument(documentId: Int, ownerId: Int, filename: String) {
        viewModelScope.launch {
            val userId = withIO { sessionManager.getUserId() }

            if (userId != ownerId && tripOverview.value?.userRole != UserRole.ADMIN) {
                showSnackbar(SnackbarInfo.error(R.string.delete_document_restricted))
                return@launch
            }

            val tripId = tripOverview.value?.id ?: return@launch
            val storedFilename = DownloadedFile.getDocumentName(documentId, filename)

            withIO {
                documentRepository.deleteDocument(tripId, documentId).safeCollect(this) {
                    when (it) {
                        is Result.Cache -> TODO()
                        is Result.Failure -> failure(it.error, documentActionLoading)
                        is Result.Loading -> documentActionLoading.value = true
                        is Result.Success -> deleteDocumentSuccess(tripId, storedFilename)
                    }
                }
            }
        }
    }

    private fun deleteDocumentSuccess(tripId: Int, storedFilename: String) {
        removeFile(storedFilename)
        loadDocuments(tripId)
        documentActionLoading.value = false
    }

    fun prepareCoordinates() {
        viewModelScope.launch {
            allCoordinates.value = mergeAllCoordinates()
        }
    }

    private suspend fun mergeAllCoordinates(): List<LatLngBundle> {
        return withDefault {
            val accommodation = accommodationCoordinates.value ?: emptyList()
            val place = placeCoordinates.value ?: emptyList()
            val transport = transportCoordinates.value ?: emptyList()
            val activity = activityCoordinates.value ?: emptyList()
            accommodation + place + transport + activity
        }
    }
}