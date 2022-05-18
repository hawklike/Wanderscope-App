package cz.cvut.fit.steuejan.wanderscope.points.transport.overview

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripOverviewBinding

class TransportOverviewFragment : MvvmFragment<
        FragmentTripOverviewBinding,
        TransportOverviewFragmentVM>(
    R.layout.fragment_point_transport_overview,
    TransportOverviewFragmentVM::class
) {
    override val hasBottomNavigation = false
    override val hasTitle = false
}