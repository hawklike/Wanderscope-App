package cz.cvut.fit.steuejan.wanderscope.trip.overview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import cz.cvut.fit.steuejan.wanderscope.MainActivityVM
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.AlertDialogInfo
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.SnackbarInfo
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.viewpager.ViewPagerFragment
import cz.cvut.fit.steuejan.wanderscope.app.binding.visibleOrGone
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.FileManager
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.app.common.data.DocumentType
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.extension.toDurationString
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.app.util.saveEventToCalendar
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripOverviewBinding
import cz.cvut.fit.steuejan.wanderscope.document.DocumentMetadataItem
import cz.cvut.fit.steuejan.wanderscope.document.model.DownloadedFile
import cz.cvut.fit.steuejan.wanderscope.points.TripPointOverviewItem
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.bundle.PointOverviewBundle
import cz.cvut.fit.steuejan.wanderscope.trip.api.response.TripResponse
import cz.cvut.fit.steuejan.wanderscope.trip.common.WithAddPointActionButton
import cz.cvut.fit.steuejan.wanderscope.trip.crud.bundle.EditTripBundle
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentDirections
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentVM
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TripOverviewFragment : ViewPagerFragment<FragmentTripOverviewBinding, TripOverviewFragmentVM>(
    R.layout.fragment_trip_overview,
    TripOverviewFragmentVM::class
), WithLoading, WithRecycler, WithAddPointActionButton {

    override val content: View get() = binding.tripOverviewContent
    override val shimmer: ShimmerFrameLayout get() = binding.tripOverviewShimmer

    private val mainVM by sharedViewModel<MainActivityVM>()

    private var tripOverview: TripResponse? = null

    private val userRole: UserRole? by lazy {
        arguments?.getSerializable(USER_ROLE) as? UserRole
    }

    private val parentViewModel by lazy {
        getParentViewModel<TripPagerFragmentVM>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        getTripOverview(Load.ALL)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        handleLoading()
        handleActionButtons()
        handlePointsRecycler()
        handleDocumentsRecycler()
        listenToChanges()
        listenToLeaveTrip()
        listenToDeleteTrip()
        listenToGoToMap()
        prepareActionButton(binding.tripOverviewAddButton)
        handleSwipeRefresh()
    }

    override fun onPause() {
        super.onPause()
        binding.tripOverviewAddButton.close()
    }

    private fun listenToChanges() {
        parentViewModel?.tripOverviewResult?.safeObserve {
            getTripOverview(it)
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                delay(250) //time to hide keyboard
                it?.getUpdatingString()?.let(::showToast)
            }
        }
        mainVM.updateDocument.safeObserve { update ->
            if (update) {
                getTripOverview(Load.DOCUMENTS)
                Load.DOCUMENTS.getUpdatingString()?.let(::showToast)
            }
        }
    }

    private fun handleLoading() {
        arguments?.getInt(TRIP_ID) ?: return
        showLoading()
        viewModel.loading.safeObserve { loading ->
            if (!loading) {
                hideLoading()
            }
        }
    }

    private fun handleActionButtons() {
        arguments?.getInt(TRIP_ID) ?: return
        hideActionButtons()
        viewModel.tripOverview.safeObserve {
            tripOverview = it
            requireActivity().invalidateOptionsMenu()
            showActionButtons(it.userRole)
        }
    }

    private fun handleSwipeRefresh() {
        binding.tripOverviewSwipeRefresh.setOnRefreshListener {
            getTripOverview(Load.ALL)
        }
    }

    private fun getTripOverview(whatToLoad: Load) {
        val tripId = arguments?.getInt(TRIP_ID) ?: return
        viewModel.getTrip(tripId, whatToLoad)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.trip_overview_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        when (tripOverview?.userRole ?: userRole) {
            UserRole.ADMIN -> {
                doNothing
            }
            UserRole.EDITOR -> {
                menu.findItem(R.id.action_trip_delete)?.isVisible = false
            }
            UserRole.VIEWER -> {
                menu.findItem(R.id.action_trip_delete)?.isVisible = false
                menu.findItem(R.id.action_trip_edit)?.isVisible = false
            }
            null -> doNothing
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_trip_edit -> editTrip()
            R.id.action_trip_delete -> deleteTrip()
            R.id.action_trip_save_to_calendar -> saveToCalendar()
            R.id.action_trip_leave -> leaveTrip()
            R.id.action_trip_travellers -> goToTravellers()
            R.id.action_trip_map -> goToMap()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun goToTravellers(): Boolean {
        tripOverview ?: return pleaseWait()
        viewModel.manageUsers()
        return true
    }

    private fun goToMap(): Boolean {
        tripOverview ?: return pleaseWait()
        viewModel.prepareCoordinates()
        return true
    }

    private fun listenToGoToMap() {
        viewModel.allCoordinates.safeObserve {
            val title = tripOverview?.name ?: return@safeObserve
            val duration = tripOverview?.duration ?: return@safeObserve
            setSharedData(it)
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                navigateTo(
                    TripPagerFragmentDirections.actionTripPagerFragmentToMapFragment(
                        title,
                        duration.toDurationString(),
                        hasBottomNavigation = false
                    )
                )
            }
        }
    }

    private fun handlePointsRecycler() {
        setAdapterListener(binding.tripOverviewTransport) { item, _ ->
            if (item is TripPointOverviewItem) {
                goToTransport(item)
            }
        }
        setAdapterListener(binding.tripOverviewPlace) { item, _ ->
            if (item is TripPointOverviewItem) {
                goToPlace(item)
            }
        }
        setAdapterListener(binding.tripOverviewActivity) { item, _ ->
            if (item is TripPointOverviewItem) {
                goToActivity(item)
            }
        }
        setAdapterListener(binding.tripOverviewAccommodation) { item, _ ->
            if (item is TripPointOverviewItem) {
                goToAccommodation(item)
            }
        }
    }

    private fun handleDocumentsRecycler() {
        setAdapterListener(
            binding.tripOverviewDocument,
            onLongClickListener = ::deleteDocument
        ) { item, _ ->
            if (item is DocumentMetadataItem) {
                val filename = DownloadedFile.getDocumentName(item.id, item.name)
                if (!FileManager(requireContext()).openFile(filename, item.type)) {
                    if (item.hasKey) {
                        showDialogWithKeyInput(item.id, item.name, item.type)
                    } else {
                        showDialogBeforeDownload(item.id, item.name, item.type)
                    }
                }
            }
        }
        listenToDocumentSuccessRequest()
    }

    private fun listenToDocumentSuccessRequest() {
        viewModel.documentSuccessRequest.safeObserve {
            binding.tripOverviewDocument.smoothScrollToPosition(0)
        }
    }

    @SuppressLint("InflateParams")
    private fun showDialogWithKeyInput(documentId: Int, filename: String, type: DocumentType) {
        val customView = LayoutInflater.from(requireContext())
            .inflate(R.layout.layout_dialog_with_password, null, false)
        val keyInput = customView.findViewById<TextInputLayout>(R.id.dialogDocumentKey)

        showAlertDialog(
            customView,
            AlertDialogInfo(
                R.string.download_document_title,
                R.string.download_document_with_key_message,
                positiveButton = R.string.download
            ) { dialog, _ ->
                val key = keyInput.editText?.text?.toString()
                dialog.dismiss()
                viewModel.downloadDocument(documentId, filename, type, key)
            })
    }

    private fun showDialogBeforeDownload(documentId: Int, filename: String, type: DocumentType) {
        showAlertDialog(
            AlertDialogInfo(
                R.string.download_document_title,
                R.string.download_document_message,
                positiveButton = R.string.download
            ) { _, _ ->
                viewModel.downloadDocument(documentId, filename, type)
            }
        )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun deleteDocument(item: RecyclerItem, position: Int) {
        if (item is DocumentMetadataItem) {
            showAlertDialog(
                AlertDialogInfo(
                    R.string.delete_document_title,
                    R.string.delete_document_message,
                    positiveButton = R.string.delete
                ) { _, _ ->
                    viewModel.deleteDocument(item.id, item.ownerId, item.name)
                })
        }
    }

    override fun openFile(file: DownloadedFile, fileManager: FileManager, type: DocumentType?): Boolean {
        viewModel.documentActionLoading.postValue(false)
        return super.openFile(file, fileManager, type)
    }

    override fun savingFileFailed() {
        super.savingFileFailed()
        viewModel.documentActionLoading.postValue(false)
    }

    private fun goToTransport(item: TripPointOverviewItem) {
        val trip = tripOverview ?: return showToast(R.string.unexpected_error_short)
        navigateTo(
            TripPagerFragmentDirections
                .actionTripPagerFragmentToTransportOverviewFragment(
                    createPointOverviewBundle(trip, item)
                )
        )
    }

    private fun goToPlace(item: TripPointOverviewItem) {
        val trip = tripOverview ?: return showToast(R.string.unexpected_error_short)
        navigateTo(
            TripPagerFragmentDirections
                .actionTripPagerFragmentToPlaceOverviewFragment(
                    createPointOverviewBundle(trip, item)
                )
        )
    }

    private fun goToAccommodation(item: TripPointOverviewItem) {
        val trip = tripOverview ?: return showToast(R.string.unexpected_error_short)
        navigateTo(
            TripPagerFragmentDirections
                .actionTripPagerFragmentToAccommodationOverviewFragment(
                    createPointOverviewBundle(trip, item)
                )
        )
    }

    private fun goToActivity(item: TripPointOverviewItem) {
        val trip = tripOverview ?: return showToast(R.string.unexpected_error_short)
        navigateTo(
            TripPagerFragmentDirections
                .actionTripPagerFragmentToActivityOverviewFragment(
                    createPointOverviewBundle(trip, item)
                )
        )
    }

    private fun createPointOverviewBundle(
        trip: TripResponse,
        item: TripPointOverviewItem
    ): PointOverviewBundle {
        return PointOverviewBundle.create(trip.id, item.id, trip.userRole, item.name)
    }

    private fun showActionButtons(userRole: UserRole) {
        binding.tripOverviewAddButton.visibleOrGone(userRole.canEdit())
        binding.tripOverviewManageTravellers.visibleOrGone(userRole.canEdit())
        binding.tripOverviewDocumentAdd.visibleOrGone(userRole.canEdit())
    }

    private fun hideActionButtons() {
        binding.tripOverviewAddButton.visibility = View.GONE
        binding.tripOverviewManageTravellers.visibility = View.GONE
        binding.tripOverviewDocumentAdd.visibility = View.GONE
    }

    private fun editTrip(): Boolean {
        val trip = tripOverview ?: return pleaseWait()
        val arg = EditTripBundle(
            trip.id,
            trip.name,
            trip.duration.startDate,
            trip.duration.endDate,
            trip.description
        )
        navigateTo(TripPagerFragmentDirections.actionTripPagerFragmentToAddTripFragment(arg))
        return true
    }

    private fun deleteTrip(): Boolean {
        val trip = tripOverview ?: return pleaseWait()
        viewModel.deleteTrip(trip.id)
        return true
    }

    private fun leaveTrip(): Boolean {
        val trip = tripOverview ?: return pleaseWait()
        showAlertDialog(AlertDialogInfo(
            R.string.leave_trip_dialog_title,
            R.string.leave_trip_dialog_message,
            positiveButton = R.string.leave,
            onClickPositive = { _, _ -> viewModel.leaveTrip(trip.id) }
        ))
        return true
    }

    private var leaveTripSnackbar: Snackbar? = null

    private fun listenToLeaveTrip() {
        viewModel.leaveTripLoading.safeObserve {
            leaveTripSnackbar = showSnackbar(
                SnackbarInfo(
                    R.string.leaving_trip,
                    length = Snackbar.LENGTH_INDEFINITE
                )
            )
        }

        viewModel.leaveTripSuccess.safeObserve {
            leaveTripSnackbar?.dismiss()
            navigateBack()
        }
    }

    private var deleteTripSnackbar: Snackbar? = null

    private fun listenToDeleteTrip() {
        viewModel.deleteTripLoading.safeObserve {
            deleteTripSnackbar = showSnackbar(
                SnackbarInfo(
                    R.string.deleting_trip,
                    length = Snackbar.LENGTH_INDEFINITE
                )
            )
        }

        viewModel.deleteTripSuccess.safeObserve {
            updateTrip()
            deleteTripSnackbar?.dismiss()
            navigateBack()
        }
    }

    private fun saveToCalendar(): Boolean {
        val trip = tripOverview ?: return pleaseWait()
        with(trip) {
            startActivitySafe(
                saveEventToCalendar(
                    duration.startDate,
                    duration.endDate,
                    allDay = true,
                    name,
                    description,
                    location = null
                )
            )
        }
        return true
    }

    private fun pleaseWait(): Boolean {
        showToast(R.string.please_wait)
        return true
    }

    private fun setTitle() {
        viewModel.title.safeObserve {
            setTitle(it)
        }
    }

    override fun addAccommodation() {
        arguments?.getInt(TRIP_ID)?.let {
            navigateTo(TripPagerFragmentDirections.actionTripPagerFragmentToAccommodationAddEditFragment(it))
        }
    }

    override fun addActivity() {
        arguments?.getInt(TRIP_ID)?.let {
            navigateTo(TripPagerFragmentDirections.actionTripPagerFragmentToActivityAddEditFragment(it))
        }
    }

    override fun addTransport() {
        arguments?.getInt(TRIP_ID)?.let {
            navigateTo(TripPagerFragmentDirections.actionTripPagerFragmentToTransportAddEditFragment(it))
        }
    }

    override fun addPlace() {
        arguments?.getInt(TRIP_ID)?.let {
            navigateTo(TripPagerFragmentDirections.actionTripPagerFragmentToPlaceAddEditFragment(it))
        }
    }

    companion object {
        fun newInstance(tripId: Int, userRole: UserRole): TripOverviewFragment {
            return TripOverviewFragment().apply {
                arguments = Bundle().apply {
                    putInt(TRIP_ID, tripId)
                    putSerializable(USER_ROLE, userRole)
                }
            }
        }

        const val POSITION = 1

        private const val TRIP_ID = "tripId"
        private const val USER_ROLE = "userRole"
    }

}