package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.viewpager.ViewPagerFragment
import cz.cvut.fit.steuejan.wanderscope.app.binding.visibleOrGone
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.util.multipleLet
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripItineraryBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.bundle.PointOverviewBundle
import cz.cvut.fit.steuejan.wanderscope.trip.common.WithAddPointActionButton
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentDirections
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentVM
import kotlinx.coroutines.delay

class TripItineraryFragment : ViewPagerFragment<FragmentTripItineraryBinding, TripItineraryFragmentVM>(
    R.layout.fragment_trip_itinerary,
    TripItineraryFragmentVM::class
), WithLoading, WithRecycler, WithAddPointActionButton {

    override val content: View get() = binding.tripItineraryContent
    override val shimmer: ShimmerFrameLayout get() = binding.tripItineraryShimmer

    val tripId: Int? by lazy { arguments?.getInt(TRIP_ID) }

    val userRole: UserRole? by lazy {
        arguments?.getSerializable(USER_ROLE) as? UserRole
    }

    private val parentViewModel by lazy {
        getParentViewModel<TripPagerFragmentVM>()
    }

    private var initScrolling = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.showItinerary(tripId ?: return)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleLoading()
        handleActionButton()
        handleRecyclerScrolling()
        handleRecyclerOnClick()
        listenToChanges()
        prepareActionButton(binding.tripItineraryAddButton)
        handleSwipeRefresh()
    }

    override fun onPause() {
        super.onPause()
        binding.tripItineraryAddButton.close()
    }

    private fun listenToChanges() {
        parentViewModel?.tripItineraryResult?.safeObserve {
            viewModel.showItinerary(tripId ?: return@safeObserve)
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                delay(250) //time to hide keyboard
                showToast(R.string.updating_itinerary)
            }
        }
    }

    private fun handleLoading() {
        showLoading()
        viewModel.loading.safeObserve { loading ->
            if (!loading) {
                hideLoading()
            }
        }
    }

    private fun handleActionButton() {
        val canEdit = userRole?.canEdit() == true
        binding.tripItineraryAddButton.visibleOrGone(canEdit)
    }

    private fun handleSwipeRefresh() {
        binding.tripItinerarySwipeRefresh.setOnRefreshListener {
            viewModel.showItinerary(tripId ?: return@setOnRefreshListener)
        }
    }

    private fun handleRecyclerScrolling() {
        viewModel.activeItemIdx.safeObserve {
            if (initScrolling) {
                scrollToPosition(binding.tripItinerary, it)
                initScrolling = false
            }
        }
    }

    private fun handleRecyclerOnClick() {
        setAdapterListener(binding.tripItinerary, R.id.itineraryItemCard) { item, _ ->
            if (item is TripItineraryItem) {
                when (item.type) {
                    TripPointType.ACCOMMODATION -> goToAccommodation(item)
                    TripPointType.ACTIVITY -> goToActivity(item)
                    TripPointType.PLACE -> goToPlace(item)
                    TripPointType.TRANSPORT -> goToTransport(item)
                }
            }
        }
    }

    private fun goToTransport(item: TripItineraryItem) {
        navigateTo(
            TripPagerFragmentDirections
                .actionTripPagerFragmentToTransportOverviewFragment(
                    createPointOverviewBundle(item) ?: return
                )
        )
    }

    private fun goToPlace(item: TripItineraryItem) {
        navigateTo(
            TripPagerFragmentDirections
                .actionTripPagerFragmentToPlaceOverviewFragment(
                    createPointOverviewBundle(item) ?: return
                )
        )
    }

    private fun goToAccommodation(item: TripItineraryItem) {
        navigateTo(
            TripPagerFragmentDirections
                .actionTripPagerFragmentToAccommodationOverviewFragment(
                    createPointOverviewBundle(item) ?: return
                )
        )
    }

    private fun goToActivity(item: TripItineraryItem) {
        navigateTo(
            TripPagerFragmentDirections
                .actionTripPagerFragmentToActivityOverviewFragment(
                    createPointOverviewBundle(item) ?: return
                )
        )
    }

    private fun createPointOverviewBundle(item: TripItineraryItem): PointOverviewBundle? {
        return multipleLet(tripId, userRole) { tripId, userRole ->
            PointOverviewBundle.create(tripId, item.id, userRole, item.name)
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
        fun newInstance(tripId: Int, userRole: UserRole): TripItineraryFragment {
            return TripItineraryFragment().apply {
                arguments = Bundle().apply {
                    putInt(TRIP_ID, tripId)
                    putSerializable(USER_ROLE, userRole)
                }
            }
        }

        const val POSITION = 0

        private const val TRIP_ID = "tripId"
        private const val USER_ROLE = "userRole"
    }
}