package cz.cvut.fit.steuejan.wanderscope.trip.crud

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripAddEditBinding
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class AddEditTripFragment : MvvmFragment<FragmentTripAddEditBinding, AddEditTripFragmentVM>(
    R.layout.fragment_trip_add_edit,
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