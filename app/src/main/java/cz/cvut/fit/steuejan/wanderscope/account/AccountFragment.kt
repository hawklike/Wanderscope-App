package cz.cvut.fit.steuejan.wanderscope.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.yariksoffice.lingver.Lingver
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.SnackbarInfo
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.app.util.getLanguage
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentAccountBinding
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import java.util.*

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
        viewModel.language.value = getLanguage()
        handleLoading()
        handleLogout()
        handleLogoutAllDevices()
        handleDeleteAccount()
        changeLanguage()
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

    private fun changeLanguage() {
        binding.accountChangeLanguageOnClick.setOnClickListener {
            createChangeLanguageDialog()
        }
    }

    private fun createChangeLanguageDialog() {
        val items = arrayOf(getString(R.string.czech), getString(R.string.english))
        val default = when (Locale.getDefault().language) {
            "cs" -> 0
            else -> 1
        }
        var selected = default
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.Widget_Wanderscope_AlertDialog_Confirmation
        )
            .setTitle(R.string.change_language)
            .setSingleChoiceItems(items, default) { _, which ->
                selected = which
            }
            .setNegativeButton(R.string.cancel) { _, _ -> doNothing }
            .setPositiveButton(R.string.change) { _, _ ->
                val language = if (selected == 0) "cs" else "en"
                Lingver.getInstance().setLocale(requireContext(), language)
                requireActivity().recreate()
            }
            .show()
    }

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "accountFragmentRequestKey"
        const val FRAGMENT_RESULT_BUNDLE = "accountFragmentBundle"
    }
}