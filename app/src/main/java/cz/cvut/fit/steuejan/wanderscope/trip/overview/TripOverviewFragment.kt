package cz.cvut.fit.steuejan.wanderscope.trip.overview

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.AlertDialogInfo
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.SnackbarInfo
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.viewpager.ViewPagerFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.app.util.saveEventToCalendar
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripOverviewBinding
import cz.cvut.fit.steuejan.wanderscope.document.DocumentMetadataItem
import cz.cvut.fit.steuejan.wanderscope.points.TripPointOverviewItem
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.bundle.PointOverviewBundle
import cz.cvut.fit.steuejan.wanderscope.trip.api.response.TripResponse
import cz.cvut.fit.steuejan.wanderscope.trip.common.WithAddPointActionButton
import cz.cvut.fit.steuejan.wanderscope.trip.crud.bundle.EditTripBundle
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentDirections
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentVM
import kotlinx.coroutines.delay

class TripOverviewFragment : ViewPagerFragment<FragmentTripOverviewBinding, TripOverviewFragmentVM>(
    R.layout.fragment_trip_overview,
    TripOverviewFragmentVM::class
), WithLoading, WithRecycler, WithAddPointActionButton {

    override val content: View get() = binding.tripOverviewContent
    override val shimmer: ShimmerFrameLayout get() = binding.tripOverviewShimmer

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
        handleActionButton()
        handlePointsRecycler()
        handleDocumentsRecycler()
        listenToChanges()
        listenToLeaveTrip()
        listenToDeleteTrip()
        prepareActionButton(binding.tripOverviewAddButton)
        handleSwipeRefresh()
    }

    private fun listenToChanges() {
        parentViewModel?.tripOverviewResult?.safeObserve {
            getTripOverview(it)
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                delay(250) //time to hide keyboard
                it?.getUpdatingString()?.let(::showToast)
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

    private fun handleActionButton() {
        arguments?.getInt(TRIP_ID) ?: return
        hideActionButton()
        viewModel.tripOverview.safeObserve {
            tripOverview = it
            showActionButton(it.userRole)
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
        when (userRole) {
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
            else -> super.onOptionsItemSelected(item)
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
        setAdapterListener(binding.tripOverviewDocument) { item, _ ->
            if (item is DocumentMetadataItem) {
                tripOverview?.id?.let {
                    viewModel.downloadDocument(it, item.id)
                }
            }
        }
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

    private fun showActionButton(userRole: UserRole) {
        val visibility = if (userRole.canEdit()) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.tripOverviewAddButton.visibility = visibility
        binding.tripOverviewManageTravellers.visibility = visibility
    }

    private fun hideActionButton() {
        binding.tripOverviewAddButton.visibility = View.GONE
        binding.tripOverviewManageTravellers.visibility = View.GONE
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

    private fun listenToDeleteTrip() {
        viewModel.deleteTripSuccess.safeObserve {
            updateTrip()
            showSnackbar(
                SnackbarInfo(
                    R.string.successfully_deleted,
                    length = Snackbar.LENGTH_SHORT
                )
            )
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