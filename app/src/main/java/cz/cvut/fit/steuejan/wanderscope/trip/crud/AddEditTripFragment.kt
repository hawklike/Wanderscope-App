package cz.cvut.fit.steuejan.wanderscope.trip.crud

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripAddEditBinding

class AddEditTripFragment : MvvmFragment<FragmentTripAddEditBinding, AddEditTripFragmentVM>(
    R.layout.fragment_trip_add_edit,
    AddEditTripFragmentVM::class
) {

    override val hasTitle = false
    override val hasBottomNavigation = false

    private val args: AddEditTripFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.editTrip?.let {
            viewModel.setupEditTrip(it)
        }
    }
}
