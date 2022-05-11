package cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseFragment
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripItineraryBinding

class TripItineraryFragment : MvvmFragment<FragmentTripItineraryBinding, TripItineraryFragmentVM>(
    R.layout.fragment_trip_itinerary,
    TripItineraryFragmentVM::class
) {

    override val hasBottomNavigation: Boolean
        get() = (parentFragment as? BaseFragment)?.hasBottomNavigation
            ?: super.hasBottomNavigation

    companion object {
        fun newInstance(): TripItineraryFragment {
            return TripItineraryFragment()
        }
    }
}