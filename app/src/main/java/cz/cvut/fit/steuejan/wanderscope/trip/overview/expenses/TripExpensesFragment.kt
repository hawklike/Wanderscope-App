package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripExpensesBinding

class TripExpensesFragment : MvvmFragment<FragmentTripExpensesBinding, TripExpensesFragmentVM>(
    R.layout.fragment_trip_expenses,
    TripExpensesFragmentVM::class
) {

    companion object {
        fun newInstance(): TripExpensesFragment {
            return TripExpensesFragment()
        }
    }
}