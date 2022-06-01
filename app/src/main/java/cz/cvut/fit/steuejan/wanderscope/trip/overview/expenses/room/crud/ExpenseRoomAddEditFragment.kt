package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.room.crud

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.lifecycleScope
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.common.WithChipGroup
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentExpenseRoomAddEditBinding
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.model.Currency

class ExpenseRoomAddEditFragment : MvvmFragment<
        FragmentExpenseRoomAddEditBinding,
        ExpenseRoomAddEditFragmentVM>(
    R.layout.fragment_expense_room_add_edit,
    ExpenseRoomAddEditFragmentVM::class
), WithChipGroup {

    override val hasTitle = false
    override val hasBottomNavigation = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareCurrencyMenu()
        handleSubmit()
    }

    private fun prepareCurrencyMenu() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val items = withDefault {
                Currency.values().map { it.getCode() }
            }
            val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_menu, items)
            (binding.expenseRoomAddCurrency.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
    }

    private fun handleSubmit() {
        viewModel.submitEvent.safeObserve {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                val members = extractChips(binding.expenseRoomAddMembersGroup).takeIf { it.isNotEmpty() }
                val request = it.copy(persons = members ?: emptyList())
                viewModel.submit(request)
            }
        }
    }

}