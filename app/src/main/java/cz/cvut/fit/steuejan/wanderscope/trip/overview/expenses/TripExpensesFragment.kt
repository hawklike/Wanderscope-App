package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses

import android.os.Bundle
import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.MainActivityVM
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.viewpager.ViewPagerFragment
import cz.cvut.fit.steuejan.wanderscope.app.binding.visibleOrGone
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripExpensesBinding
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.room.ExpenseRoomItem
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TripExpensesFragment : ViewPagerFragment<FragmentTripExpensesBinding, TripExpensesFragmentVM>(
    R.layout.fragment_trip_expenses,
    TripExpensesFragmentVM::class
), WithLoading, WithRecycler {

    override val content: View get() = binding.tripExpensesContent
    override val shimmer: ShimmerFrameLayout get() = binding.tripExpensesShimmer

    private val mainVM by sharedViewModel<MainActivityVM>()

    private val userRole: UserRole? by lazy {
        arguments?.getSerializable(USER_ROLE) as? UserRole
    }

    private val tripId: Int? by lazy {
        arguments?.getInt(TRIP_ID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadRooms(tripId ?: return)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleLoading()
        handleActionButton()
        handleSwipeRefresh()
        listenToChanges()
        handleRecyclerOnClick()
    }

    private fun handleLoading() {
        showLoading()
        viewModel.loading.safeObserve { loading ->
            if (!loading) {
                hideLoading()
            }
        }
    }

    private fun handleActionButton() {
        val canEdit = userRole?.canEdit() == true
        binding.tripExpensesAdd.visibleOrGone(canEdit)
    }

    private fun handleSwipeRefresh() {
        binding.tripExpensesSwipeRefresh.setOnRefreshListener {
            viewModel.loadRooms(tripId ?: return@setOnRefreshListener)
        }
    }

    private fun listenToChanges() {
        mainVM.updateExpenseRoom.safeObserve { update ->
            if (update) {
                viewModel.loadRooms(tripId ?: return@safeObserve)
            }
        }
    }

    private fun handleRecyclerOnClick() {
        setAdapterListener(binding.tripExpensesRecycler) { item, _ ->
            if (item is ExpenseRoomItem) {
                showAlertDialog(BaseViewModel.AlertDialogInfo(
                    R.string.stay_tuned,
                    R.string.expenses_beta_version_dialog_text,
                    positiveButton = R.string.understand
                ) { _, _ -> doNothing })
            }
        }
    }

    companion object {
        fun newInstance(tripId: Int, userRole: UserRole): TripExpensesFragment {
            return TripExpensesFragment().apply {
                arguments = Bundle().apply {
                    putInt(TRIP_ID, tripId)
                    putSerializable(USER_ROLE, userRole)
                }
            }
        }

        const val POSITION = 2

        private const val TRIP_ID = "tripId"
        private const val USER_ROLE = "userRole"
    }
}