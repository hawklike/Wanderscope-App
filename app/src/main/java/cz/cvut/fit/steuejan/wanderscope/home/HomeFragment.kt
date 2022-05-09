package cz.cvut.fit.steuejan.wanderscope.home

import android.os.Bundle
import android.view.View
import cz.cvut.fit.steuejan.wanderscope.MainActivityVM
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class HomeFragment : MvvmFragment<FragmentHomeBinding, HomeFragmentVM>(
    R.layout.fragment_home,
    HomeFragmentVM::class
) {

    private val mainVM by sharedViewModel<MainActivityVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleLogin()
        handleLogout()
    }

    private fun handleLogin() {
        viewModel.shouldLogin.safeObserve { shouldLogin ->
            if (shouldLogin) {
                login()
            } else {
                mainVM.hideSplashScreen()
            }
        }
    }

    private fun handleLogout() {
        viewModel.logout.safeObserve {
            logout()
        }
    }

}