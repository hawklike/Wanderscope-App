package cz.cvut.fit.steuejan.wanderscope.trip.overview

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripOverviewBinding

class TripOverviewFragment : MvvmFragment<FragmentTripOverviewBinding, TripOverviewFragmentVM>(
    R.layout.fragment_trip_overview,
    TripOverviewFragmentVM::class
) {

    companion object {
        fun newInstance(): TripOverviewFragment {
            return TripOverviewFragment()
        }
    }
}