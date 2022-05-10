package cz.cvut.fit.steuejan.wanderscope.trip

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripAddBinding

class AddTripFragment : MvvmFragment<FragmentTripAddBinding, AddTripFragmentVM>(
    R.layout.fragment_trip_add,
    AddTripFragmentVM::class
) {
    override val hasTitle = false
    override val hasBottomNavigation = false
}