package cz.cvut.fit.steuejan.wanderscope.trip.overview

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.viewpager.ViewPagerFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.app.util.saveEventToCalendar
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripOverviewBinding
import cz.cvut.fit.steuejan.wanderscope.points.TripPointOverviewItem
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.bundle.PointOverviewBundle
import cz.cvut.fit.steuejan.wanderscope.trip.api.response.TripResponse
import cz.cvut.fit.steuejan.wanderscope.trip.common.WithAddPointActionButton
import cz.cvut.fit.steuejan.wanderscope.trip.crud.bundle.EditTripBundle
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentDirections

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        handlePointsRecycler()
        handleLoadingNecessaryData()
        prepareActionButton(binding.tripOverviewAddButton)
    }

    private fun handleLoadingNecessaryData() {
        getViewPagerSharedData<Load>()?.safeObserve {
            it ?: return@safeObserve
            if (it != Load.NOTHING) {
                setViewPagerSharedData(Load.NOTHING)
                val load = if (tripOverview == null) Load.ALL else it
                retrieveTripOverview(load)
            }
        }
    }

    private fun retrieveTripOverview(whatToLoad: Load) {
        val tripId = arguments?.getInt(TRIP_ID) ?: return
        showLoading()
        hideActionButton()

        viewModel.getTrip(tripId, whatToLoad)

        viewModel.loading.safeObserve { loading ->
            if (!loading) {
                hideLoading()
            }
        }

        viewModel.tripOverview.safeObserve {
            tripOverview = it
            showActionButton(it.userRole)
        }
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
    }

    private fun hideActionButton() {
        binding.tripOverviewAddButton.visibility = View.GONE
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

        private const val TRIP_ID = "tripId"
        private const val USER_ROLE = "userRole"
    }

}