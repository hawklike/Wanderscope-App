package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.viewpager.ViewPagerFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.util.multipleLet
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripItineraryBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.bundle.PointOverviewBundle
import cz.cvut.fit.steuejan.wanderscope.trip.common.WithAddPointActionButton
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentDirections
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentVM

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
    }

    private fun listenToChanges() {
        parentViewModel?.tripItineraryResult?.safeObserve {
            viewModel.showItinerary(tripId ?: return@safeObserve)
            showToast(R.string.updating_itinerary)
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
        val visibility = if (userRole?.canEdit() == true) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.tripItineraryAddButton.visibility = visibility
    }

    private fun handleRecyclerScrolling() {
        viewModel.activeItemIdx.safeObserve {
            if (initScrolling) {
                (binding.tripItinerary.layoutManager as? LinearLayoutManager)
                    ?.scrollToPositionWithOffset(it, 0)
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

        private const val TRIP_ID = "tripId"
        private const val USER_ROLE = "userRole"
    }
}