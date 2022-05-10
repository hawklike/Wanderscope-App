package cz.cvut.fit.steuejan.wanderscope.trip.add_edit

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripAddBinding
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class AddTripFragment : MvvmFragment<FragmentTripAddBinding, AddEditTripFragmentVM>(
    R.layout.fragment_trip_add,
    AddEditTripFragmentVM::class
) {

    override val viewModel: AddEditTripFragmentVM by inject {
        parametersOf(
            R.string.add_trip,
            AddEditTripFragmentVM.Purpose.CREATE
        )
    }

    override val hasTitle = false
    override val hasBottomNavigation = false
}