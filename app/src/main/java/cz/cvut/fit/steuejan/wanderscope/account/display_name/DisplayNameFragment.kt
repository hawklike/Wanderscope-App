package cz.cvut.fit.steuejan.wanderscope.account.display_name

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.account.AccountFragment
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentChangeDisplayNameBinding
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load

class DisplayNameFragment : MvvmFragment<
        FragmentChangeDisplayNameBinding,
        DisplayNameFragmentVM>(
    R.layout.fragment_change_display_name,
    DisplayNameFragmentVM::class
) {
    override val hasBottomNavigation = false
    override val hasTitle = false

    private val args: DisplayNameFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(args.displayName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToSuccess()
    }

    private fun listenToSuccess() {
        viewModel.requestIsSuccess.safeObserve {
            setFragmentResult(
                AccountFragment.FRAGMENT_RESULT_REQUEST_KEY,
                bundleOf(AccountFragment.FRAGMENT_RESULT_BUNDLE to Load.ALL)
            )
            navigateBack()
        }
    }
}