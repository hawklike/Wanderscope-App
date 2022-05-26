package cz.cvut.fit.steuejan.wanderscope.trips

import android.os.Bundle
import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripsBinding

class TripsFragment : MvvmFragment<FragmentTripsBinding, TripsFragmentVM>(
    R.layout.fragment_trips,
    TripsFragmentVM::class
), WithRecycler, WithLoading {

    override val hasToolbar = false

    override val content: View get() = binding.tripsContent
    override val shimmer: ShimmerFrameLayout get() = binding.tripsShimmer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleTripsRecycler()
        retrieveTrips()
        handleSwipeRefresh()
    }

    private fun retrieveTrips() {
        showLoading()
        viewModel.getTrips()
        viewModel.loading.safeObserve { loading ->
            if (!loading) {
                hideLoading()
            }
        }
    }

    private fun handleSwipeRefresh() {
        binding.tripsSwipeRefresh.setOnRefreshListener {
            viewModel.getTrips()
        }
    }

    private fun handleTripsRecycler() {
        setAdapterListener(binding.tripsUpcomingTrips) { item, _ ->
            goToTrip(item)
        }
        setAdapterListener(binding.tripsPastTrips) { item, _ ->
            goToTrip(item)
        }
    }

    private fun goToTrip(trip: RecyclerItem) {
        if (trip is TripOverviewItem) {
            navigateTo(
                TripsFragmentDirections.actionTripsFragmentToTripPagerFragment(
                    trip.id,
                    trip.name,
                    hasBottomNavigation = false,
                    trip.userRole
                )
            )
        }
    }
}