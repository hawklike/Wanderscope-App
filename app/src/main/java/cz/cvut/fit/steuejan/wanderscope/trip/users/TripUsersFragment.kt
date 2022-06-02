package cz.cvut.fit.steuejan.wanderscope.trip.users

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.binding.visibleOrGone
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.app.common.Purpose
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentUsersManageBinding
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragment
import cz.cvut.fit.steuejan.wanderscope.user.UserItem

class TripUsersFragment : MvvmFragment<
        FragmentUsersManageBinding,
        TripUsersFragmentVM>(
    R.layout.fragment_users_manage,
    TripUsersFragmentVM::class
), WithLoading, WithRecycler {

    override val hasBottomNavigation = false
    override val hasTitle = false

    override val content: View get() = binding.manageUsersContent
    override val shimmer: ShimmerFrameLayout get() = binding.manageUsersShimmer

    private val args: TripUsersFragmentArgs by navArgs()

    private var userRole: UserRole? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFragmentResultListener()
        viewModel.loadUsers(args.tripId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleLoading()
        listenToUserRole()
        handleActionButton()
        handleRecyclerOnClick()
        handleSwipeRefresh()
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener(USERS_UPDATED_REQUEST_KEY) { _, bundle ->
            val result = bundle.getParcelable<Load>(USERS_UPDATED_RESULT_BUNDLE)
                ?: return@setFragmentResultListener
            if (result == Load.USERS) {
                viewModel.loadUsers(args.tripId)
                showToast(R.string.updating_users)
                setFragmentResult(
                    TripPagerFragment.TRIP_UPDATED_REQUEST_KEY,
                    bundleOf(TripPagerFragment.TRIP_UPDATED_RESULT_BUNDLE to result)
                )
            }
        }
    }

    private fun listenToUserRole() {
        viewModel.userRole.safeObserve {
            this.userRole = it
            handleActionButton()
        }
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
        val role = userRole ?: args.userRole
        binding.manageUsersAdd.visibleOrGone(role.canEdit())
        listenToAddUserClick()
    }

    private fun handleSwipeRefresh() {
        binding.manageUsersSwipeRefresh.setOnRefreshListener {
            viewModel.loadUsers(args.tripId)
        }
    }

    private fun listenToAddUserClick() {
        viewModel.addUserEvent.safeObserve { purpose ->
            navigateTo(
                TripUsersFragmentDirections
                    .actionTripUsersFragmentToTripUsersAddEditFragment(
                        args.tripId, userRole ?: args.userRole, purpose
                    )
            )
        }
    }

    private fun handleRecyclerOnClick() {
        setAdapterListener(binding.manageUsersRecycler) { user, _ ->
            if (user is UserItem && user.hasMenu) {
                navigateTo(
                    TripUsersFragmentDirections
                        .actionTripUsersFragmentToTripUsersAddEditFragment(
                            args.tripId, user.role, Purpose.EDIT, user.name, user.id
                        )
                )
            }
        }
    }

    companion object {
        const val USERS_UPDATED_REQUEST_KEY = "usersUpdatedRequestKey"
        const val USERS_UPDATED_RESULT_BUNDLE = "usersUpdatedResultBundle"
    }
}