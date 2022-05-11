package cz.cvut.fit.steuejan.wanderscope.trip.overview

import android.os.Bundle
import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseFragment
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripOverviewBinding

class TripOverviewFragment : MvvmFragment<FragmentTripOverviewBinding, TripOverviewFragmentVM>(
    R.layout.fragment_trip_overview,
    TripOverviewFragmentVM::class
), WithLoading {

    override val hasBottomNavigation: Boolean
        get() = (parentFragment as? BaseFragment)?.hasBottomNavigation
            ?: super.hasBottomNavigation

    override val content: View get() = binding.tripOverviewContent
    override val shimmer: ShimmerFrameLayout get() = binding.tripOverviewShimmer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveTripOverview()
    }

    private fun retrieveTripOverview() {
        showLoading()
        viewModel.getTrip(arguments?.getInt(TRIP_ID) ?: return)
        viewModel.loading.safeObserve { loading ->
            if (!loading) {
                hideLoading()
            }
        }
    }

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