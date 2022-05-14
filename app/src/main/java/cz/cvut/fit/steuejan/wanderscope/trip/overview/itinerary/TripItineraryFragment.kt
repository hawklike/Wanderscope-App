package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.viewpager.ViewPagerFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripItineraryBinding

class TripItineraryFragment : ViewPagerFragment<FragmentTripItineraryBinding, TripItineraryFragmentVM>(
    R.layout.fragment_trip_itinerary,
    TripItineraryFragmentVM::class
) {

    companion object {
        fun newInstance(): TripItineraryFragment {
            return TripItineraryFragment()
        }
    }
}