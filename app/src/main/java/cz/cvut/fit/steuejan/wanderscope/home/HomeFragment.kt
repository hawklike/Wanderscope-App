package cz.cvut.fit.steuejan.wanderscope.home

import android.os.Bundle
import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.MainActivityVM
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.util.multipleLet
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentHomeBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.bundle.PointOverviewBundle
import cz.cvut.fit.steuejan.wanderscope.trip.common.WithAddPointActionButton
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.TripItineraryItem
import cz.cvut.fit.steuejan.wanderscope.trips.api.response.TripOverviewResponse
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class HomeFragment : MvvmFragment<FragmentHomeBinding, HomeFragmentVM>(
    R.layout.fragment_home,
    HomeFragmentVM::class
), WithLoading, WithRecycler, WithAddPointActionButton {

    override val hasToolbar = false

    override val content: View get() = binding.homeContent
    override val shimmer: ShimmerFrameLayout get() = binding.homeShimmer

    private val mainVM by sharedViewModel<MainActivityVM>()

    private var initScrolling = true

    private var tripOverview: TripOverviewResponse? = null

    private val tripEmptyTitle by lazy {
        getString(R.string.no_upcoming_trips_title)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getRecommendedTrip(tripEmptyTitle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleLogin()
        handleLoading()
        handleActionButton()
        updateTripOverview()
        handleRecyclerScrolling()
        handleRecyclerOnClick()
        listenToChanges()
        prepareActionButton(binding.homeAddButton)
        handleSwipeRefresh()
    }

    private fun listenToChanges() {
        mainVM.updateTrip.safeObserve { update ->
            if (update) {
                viewModel.getRecommendedTrip(tripEmptyTitle)
            }
        }
        mainVM.updateTripPoint.safeObserve { update ->
            if (update) {
                viewModel.getItinerary(tripOverview?.id ?: return@safeObserve)
            }
        }
    }

    private fun handleLogin() {
        viewModel.shouldLogin.safeObserve { shouldLogin ->
            if (shouldLogin) {
                login()
            } else {
                mainVM.hideSplashScreen()
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

    private fun handleRecyclerScrolling() {
        viewModel.activeItemIdx.safeObserve {
            if (initScrolling) {
                scrollToPosition(binding.homeItinerary, it)
                initScrolling = false
            }
        }
    }

    private fun updateTripOverview() {
        viewModel.tripOverview.safeObserve {
            tripOverview = it
        }
    }

    private fun handleSwipeRefresh() {
        binding.homeSwipeRefresh.setOnRefreshListener {
            viewModel.getItinerary(tripOverview?.id ?: return@setOnRefreshListener)
        }
    }

    private fun handleRecyclerOnClick() {
        setAdapterListener(binding.homeItinerary, R.id.itineraryItemCard) { item, _ ->
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

    private fun createPointOverviewBundle(item: TripItineraryItem): PointOverviewBundle? {
        return multipleLet(tripOverview?.id, tripOverview?.role) { tripId, userRole ->
            PointOverviewBundle.create(tripId, item.id, userRole, item.name)
        }
    }

    private fun goToTransport(item: TripItineraryItem) {
        navigateTo(
            HomeFragmentDirections
                .actionHomeFragmentToTransportOverviewFragment(
                    createPointOverviewBundle(item) ?: return
                )
        )
    }

    private fun goToPlace(item: TripItineraryItem) {
        navigateTo(
            HomeFragmentDirections
                .actionHomeFragmentToPlaceOverviewFragment(
                    createPointOverviewBundle(item) ?: return
                )
        )
    }

    private fun goToAccommodation(item: TripItineraryItem) {
        navigateTo(
            HomeFragmentDirections
                .actionHomeFragmentToAccommodationOverviewFragment(
                    createPointOverviewBundle(item) ?: return
                )
        )
    }

    private fun goToActivity(item: TripItineraryItem) {
        navigateTo(
            HomeFragmentDirections
                .actionHomeFragmentToActivityOverviewFragment(
                    createPointOverviewBundle(item) ?: return
                )
        )
    }

    private fun handleActionButton() {
        binding.homeAddButton.visibility = View.GONE
        viewModel.tripOverview.safeObserve {
            tripOverview = it
            showActionButton(it.role)
        }
    }

    private fun showActionButton(userRole: UserRole) {
        val visibility = if (userRole.canEdit()) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.homeAddButton.visibility = visibility
    }

    override fun addAccommodation() {
        navigateTo(
            HomeFragmentDirections.actionHomeFragmentToAccommodationAddEditFragment(
                tripOverview?.id ?: return
            )
        )
    }

    override fun addActivity() {
        navigateTo(
            HomeFragmentDirections.actionHomeFragmentToActivityAddEditFragment(
                tripOverview?.id ?: return
            )
        )
    }

    override fun addTransport() {
        navigateTo(
            HomeFragmentDirections.actionHomeFragmentToTransportAddEditFragment(
                tripOverview?.id ?: return
            )
        )
    }

    override fun addPlace() {
        navigateTo(
            HomeFragmentDirections.actionHomeFragmentToPlaceAddEditFragment(
                tripOverview?.id ?: return
            )
        )
    }
}