package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary

import android.os.Bundle
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.viewpager.ViewPagerFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripItineraryBinding

class TripItineraryFragment : ViewPagerFragment<FragmentTripItineraryBinding, TripItineraryFragmentVM>(
    R.layout.fragment_trip_itinerary,
    TripItineraryFragmentVM::class
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showItinerary()
    }

    private fun showItinerary() {
        val tripId = arguments?.getInt(TRIP_ID) ?: return
        viewModel.showItinerary(tripId)
    }

    companion object {
        fun newInstance(tripId: Int): TripItineraryFragment {
            return TripItineraryFragment().apply {
                arguments = Bundle().apply {
                    putInt(TRIP_ID, tripId)
                }
            }
        }

        private const val TRIP_ID = "tripId"
    }
}