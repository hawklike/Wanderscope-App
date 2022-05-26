package cz.cvut.fit.steuejan.wanderscope.trip.users

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentUsersManageBinding

class TripUsersFragment : MvvmFragment<
        FragmentUsersManageBinding,
        TripUsersFragmentVM>(
    R.layout.fragment_users_manage,
    TripUsersFragmentVM::class
), WithLoading {

    override val hasBottomNavigation = false
    override val hasTitle = false

    override val content: View get() = binding.manageUsersContent
    override val shimmer: ShimmerFrameLayout get() = binding.manageUsersShimmer

    private val args: TripUsersFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUsers(args.tripId, args.userRole)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleLoading()
    }

    private fun handleLoading() {
        showLoading()
        viewModel.usersLoading.safeObserve { loading ->
            if (!loading) {
                hideLoading()
            }
        }
    }

}