package cz.cvut.fit.steuejan.wanderscope.trips

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripsBinding

class TripsFragment : MvvmFragment<FragmentTripsBinding, TripsFragmentVM>(
    R.layout.fragment_trips,
    TripsFragmentVM::class
) {
    override val hasToolbar = false
}