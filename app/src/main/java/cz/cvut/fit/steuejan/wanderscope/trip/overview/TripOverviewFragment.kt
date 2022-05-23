package cz.cvut.fit.steuejan.wanderscope.trip.overview

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.facebook.shimmer.ShimmerFrameLayout
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
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
import cz.cvut.fit.steuejan.wanderscope.trip.crud.bundle.EditTripBundle
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentDirections

class TripOverviewFragment : ViewPagerFragment<FragmentTripOverviewBinding, TripOverviewFragmentVM>(
    R.layout.fragment_trip_overview,
    TripOverviewFragmentVM::class
), WithLoading, WithRecycler {

    override val content: View get() = binding.tripOverviewContent
    override val shimmer: ShimmerFrameLayout get() = binding.tripOverviewShimmer

    private var tripOverview: TripResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        handlePointsRecycler()
        handleLoadingNecessaryData()
        prepareActionButton()
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
        when (tripOverview?.userRole) {
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
            goToTransport(item)
        }
        setAdapterListener(binding.tripOverviewPlace) { item, _ ->
            goToPlace(item)
        }
        setAdapterListener(binding.tripOverviewActivity) { item, _ ->
            goToActivity(item)
        }
        setAdapterListener(binding.tripOverviewAccommodation) { item, _ ->
            goToAccommodation(item)
        }
    }

    private fun goToTransport(item: RecyclerItem) {
        if (item is TripPointOverviewItem) {
            val trip = tripOverview ?: return showToast(R.string.unexpected_error_short)
            navigateTo(
                TripPagerFragmentDirections
                    .actionTripPagerFragmentToTransportOverviewFragment(
                        PointOverviewBundle.create(trip, item)
                    )
            )
        }
    }

    private fun goToPlace(item: RecyclerItem) {
        if (item is TripPointOverviewItem) {
            val trip = tripOverview ?: return showToast(R.string.unexpected_error_short)
            navigateTo(
                TripPagerFragmentDirections
                    .actionTripPagerFragmentToPlaceOverviewFragment(
                        PointOverviewBundle.create(trip, item)
                    )
            )
        }
    }

    private fun goToAccommodation(item: RecyclerItem) {
        if (item is TripPointOverviewItem) {
            val trip = tripOverview ?: return showToast(R.string.unexpected_error_short)
            navigateTo(
                TripPagerFragmentDirections
                    .actionTripPagerFragmentToAccommodationOverviewFragment(
                        PointOverviewBundle.create(trip, item)
                    )
            )
        }
    }

    private fun goToActivity(item: RecyclerItem) {
        if (item is TripPointOverviewItem) {
            val trip = tripOverview ?: return showToast(R.string.unexpected_error_short)
            navigateTo(
                TripPagerFragmentDirections
                    .actionTripPagerFragmentToActivityOverviewFragment(
                        PointOverviewBundle.create(trip, item)
                    )
            )
        }
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

    private fun prepareActionButton() {
        val actionButton = binding.tripOverviewAddButton
        actionButton.apply {
            createActionItem(R.id.add_point_transport, R.drawable.ic_outline_plane, R.string.add_transport)
            createActionItem(R.id.add_point_accommodation, R.drawable.ic_outline_house, R.string.add_accommodation)
            createActionItem(R.id.add_point_place, R.drawable.ic_outline_location, R.string.add_place)
            createActionItem(R.id.add_point_activity, R.drawable.ic_outline_hiking, R.string.add_activity)
        }

        actionButton.apply {
            setOnActionSelectedListener {
                when (it.id) {
                    R.id.add_point_accommodation -> onClick(::addAccommodation)
                    R.id.add_point_activity -> onClick(::addActivity)
                    R.id.add_point_transport -> onClick(::addTransport)
                    R.id.add_point_place -> onClick(::addPlace)
                    else -> onClick(doNothing)
                }
                false
            }
        }
    }

    private fun SpeedDialView.onClick(action: () -> Unit): Boolean {
        action.invoke()
        close()
        return false
    }

    private fun addAccommodation() {
        arguments?.getInt(TRIP_ID)?.let {
            navigateTo(TripPagerFragmentDirections.actionTripPagerFragmentToAccommodationAddEditFragment(it))
        }
    }

    private fun addActivity() {
        arguments?.getInt(TRIP_ID)?.let {
            navigateTo(TripPagerFragmentDirections.actionTripPagerFragmentToActivityAddEditFragment(it))
        }
    }

    private fun addTransport() {
        arguments?.getInt(TRIP_ID)?.let {
            navigateTo(TripPagerFragmentDirections.actionTripPagerFragmentToTransportAddEditFragment(it))
        }
    }

    private fun addPlace() {
        arguments?.getInt(TRIP_ID)?.let {
            navigateTo(TripPagerFragmentDirections.actionTripPagerFragmentToPlaceAddEditFragment(it))
        }
    }

    private fun SpeedDialView.createActionItem(
        @IdRes id: Int,
        @DrawableRes icon: Int,
        @StringRes title: Int
    ) = addActionItem(
        SpeedDialActionItem.Builder(id, icon)
            .setFabImageTintColor(requireContext().getColor(R.color.colorBackground))
            .setLabel(title)
            .setLabelColor(requireContext().getColor(R.color.colorBackground))
            .setLabelBackgroundColor(requireContext().getColor(R.color.colorPrimary))
            .create()
    )

    companion object {
        fun newInstance(tripId: Int): TripOverviewFragment {
            return TripOverviewFragment().apply {
                arguments = Bundle().apply {
                    putInt(TRIP_ID, tripId)
                }
            }
        }

        private const val TRIP_ID = "tripId"
    }

}