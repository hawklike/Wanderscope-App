package cz.cvut.fit.steuejan.wanderscope.trip.users.crud

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentUsersAddBinding
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.users.TripUsersFragment
import timber.log.Timber

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
        viewModel.init(args.purpose, args.userRole, args.tripId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToSuccess()
    }

    private fun listenToSuccess() {
        viewModel.requestIsSuccess.safeObserve {
            Timber.d("here")
            setFragmentResult(
                TripUsersFragment.USERS_UPDATED_REQUEST_KEY,
                bundleOf(TripUsersFragment.USERS_UPDATED_RESULT_BUNDLE to Load.USERS)
            )
            navigateBack()
        }
    }
}