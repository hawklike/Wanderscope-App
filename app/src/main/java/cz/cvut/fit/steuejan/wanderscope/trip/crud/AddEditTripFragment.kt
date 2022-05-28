package cz.cvut.fit.steuejan.wanderscope.trip.crud

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripAddEditBinding
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragment

class AddEditTripFragment : MvvmFragment<FragmentTripAddEditBinding, AddEditTripFragmentVM>(
    R.layout.fragment_trip_add_edit,
    AddEditTripFragmentVM::class
) {

    override val hasTitle = false
    override val hasBottomNavigation = false

    private val args: AddEditTripFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEditTrip()
        handleSucess()
    }

    private fun setupEditTrip() {
        args.editTrip?.let {
            viewModel.setupEditTrip(it)
        }
    }

    private fun handleSucess() {
        viewModel.requestIsSuccess.safeObserve {
            updateTrip()
            setFragmentResult(
                TripPagerFragment.TRIP_UPDATED_REQUEST_KEY,
                bundleOf(TripPagerFragment.TRIP_UPDATED_RESULT_BUNDLE to Load.TRIP)
            )
            navigateBack()
        }
    }
}
