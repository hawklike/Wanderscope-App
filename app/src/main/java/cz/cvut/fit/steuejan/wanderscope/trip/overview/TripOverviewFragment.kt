package cz.cvut.fit.steuejan.wanderscope.trip.overview

import android.os.Bundle
import android.view.View
import android.widget.Toast
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripOverviewBinding

class TripOverviewFragment : MvvmFragment<FragmentTripOverviewBinding, TripOverviewFragmentVM>(
    R.layout.fragment_trip_overview,
    TripOverviewFragmentVM::class
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), arguments?.getInt(TRIP_ID)?.toString(), Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(tripId: Int): TripOverviewFragment {
            return TripOverviewFragment().apply {
                arguments = Bundle().apply {
                    putInt(TRIP_ID, tripId)
                }
            }
        }

        private const val TRIP_ID = "tripId"
    }

}