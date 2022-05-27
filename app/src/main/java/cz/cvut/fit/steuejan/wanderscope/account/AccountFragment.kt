package cz.cvut.fit.steuejan.wanderscope.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.SnackbarInfo
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentAccountBinding
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load

class AccountFragment : MvvmFragment<FragmentAccountBinding, AccountFragmentVM>(
    R.layout.fragment_account,
    AccountFragmentVM::class
), WithLoading {

    override val hasToolbar = false

    override val content: View get() = binding.accountContent
    override val shimmer: ShimmerFrameLayout get() = binding.accountShimmer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFragmentResultListener()
        viewModel.getAccount()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleLoading()
        handleLogout()
        handleLogoutAllDevices()
        handleDeleteAccount()
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener(FRAGMENT_RESULT_REQUEST_KEY) { _, bundle ->
            val result = bundle.getParcelable<Load>(FRAGMENT_RESULT_BUNDLE)
                ?: return@setFragmentResultListener
            if (result == Load.ALL) {
                viewModel.getAccount()
                showToast(R.string.updating_display_name)
            }
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

    private fun handleLogout() {
        viewModel.logoutEvent.safeObserve {
            logout()
        }
    }

    private var logoutAllSnackbar: Snackbar? = null

    private fun handleLogoutAllDevices() {
        viewModel.logoutAllLoading.safeObserve {
            logoutAllSnackbar = showSnackbar(
                SnackbarInfo(
                    R.string.logging_out,
                    length = Snackbar.LENGTH_INDEFINITE
                )
            )
        }
        viewModel.logoutAllSuccess.safeObserve {
            logoutAllSnackbar?.dismiss()
            logout()
        }
    }

    private var deleteAccountSnackbar: Snackbar? = null

    private fun handleDeleteAccount() {
        viewModel.deleteAccountLoading.safeObserve {
            deleteAccountSnackbar = showSnackbar(
                SnackbarInfo(
                    R.string.deleting_account,
                    length = Snackbar.LENGTH_INDEFINITE
                )
            )
        }
        viewModel.deleteAccountSuccess.safeObserve {
            deleteAccountSnackbar?.dismiss()
            logout()
        }
    }

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "accountFragmentRequestKey"
        const val FRAGMENT_RESULT_BUNDLE = "accountFragmentBundle"
    }
}