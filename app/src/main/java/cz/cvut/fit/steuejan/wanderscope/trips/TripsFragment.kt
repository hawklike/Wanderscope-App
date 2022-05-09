package cz.cvut.fit.steuejan.wanderscope.trips

import android.os.Bundle
import android.view.View
import android.widget.Toast
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripsBinding

class TripsFragment : MvvmFragment<FragmentTripsBinding, TripsFragmentVM>(
    R.layout.fragment_trips,
    TripsFragmentVM::class
), WithRecycler {
    override val hasToolbar = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleTripsRecycler()
    }

    private fun handleTripsRecycler() {
        setAdapterListener(binding.tripsUpcomingTrips) { item, position ->
            item as TripOverviewItem
            Toast.makeText(requireContext(), item.id.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}