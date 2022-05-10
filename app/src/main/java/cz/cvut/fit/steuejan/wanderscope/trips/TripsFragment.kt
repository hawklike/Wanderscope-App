package cz.cvut.fit.steuejan.wanderscope.trips

import android.os.Bundle
import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripsBinding

class TripsFragment : MvvmFragment<FragmentTripsBinding, TripsFragmentVM>(
    R.layout.fragment_trips,
    TripsFragmentVM::class
), WithRecycler, WithLoading {

    override val hasToolbar = false

    override val content: View get() = binding.content
    override val shimmer: ShimmerFrameLayout get() = binding.shimmer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleTripsRecycler()
        viewModel.getTrips()
    }

    private fun handleTripsRecycler() {
        setAdapterListener(binding.tripsUpcomingTrips) { item, _ ->
            if (item is TripOverviewItem) {
                navigateTo(TripsFragmentDirections.actionTripsFragmentToTripPagerFragment())
            }
        }
    }
}