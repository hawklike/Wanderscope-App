package cz.cvut.fit.steuejan.wanderscope.trip.overview

import android.os.Bundle
import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.viewpager.ViewPagerFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripOverviewBinding

class TripOverviewFragment : ViewPagerFragment<FragmentTripOverviewBinding, TripOverviewFragmentVM>(
    R.layout.fragment_trip_overview,
    TripOverviewFragmentVM::class
), WithLoading {

    override val content: View get() = binding.tripOverviewContent
    override val shimmer: ShimmerFrameLayout get() = binding.tripOverviewShimmer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        retrieveTripOverview()
    }

    private fun setTitle() {
        viewModel.title.safeObserve {
            setTitle(it)
        }
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