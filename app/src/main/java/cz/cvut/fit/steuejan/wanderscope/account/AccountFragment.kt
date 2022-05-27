package cz.cvut.fit.steuejan.wanderscope.account

import android.os.Bundle
import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentAccountBinding

class AccountFragment : MvvmFragment<FragmentAccountBinding, AccountFragmentVM>(
    R.layout.fragment_account,
    AccountFragmentVM::class
), WithLoading {

    override val hasToolbar = false

    override val content: View get() = binding.accountContent
    override val shimmer: ShimmerFrameLayout get() = binding.accountShimmer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAccount()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleLoading()
        handleLogout()
    }

    private fun handleLoading() {
        showLoading()
        viewModel.loading.safeObserve { loading ->
            if (!loading) {
                hideLoading()
            }
        }
    }

    private fun handleLogout() {
        viewModel.logoutEvent.safeObserve {
            logout()
        }
    }
}