package cz.cvut.fit.steuejan.wanderscope.trip.users.crud

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.common.Purpose
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentUsersAddBinding
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.users.TripUsersFragment

class TripUsersAddEditFragment : MvvmFragment<
        FragmentUsersAddBinding,
        TripUsersAddEditFragmentVM>(
    R.layout.fragment_users_add,
    TripUsersAddEditFragmentVM::class
) {
    override val hasBottomNavigation = false
    override val hasTitle = false

    private val args: TripUsersAddEditFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(
            args.purpose,
            args.userRole,
            args.tripId,
            createSubtitle(args.purpose, args.username),
            args.whomId
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToSuccess()
    }

    private fun createSubtitle(purpose: Purpose, username: String?): String {
        return when (purpose) {
            Purpose.ADD -> getString(R.string.add_user_subtitle)
            Purpose.EDIT -> getString(R.string.edit_user_role_subtitle, username, username)
        }
    }

    private fun listenToSuccess() {
        viewModel.requestIsSuccess.safeObserve {
            setFragmentResult(
                TripUsersFragment.USERS_UPDATED_REQUEST_KEY,
                bundleOf(TripUsersFragment.USERS_UPDATED_RESULT_BUNDLE to Load.USERS)
            )
            navigateBack()
        }
    }
}