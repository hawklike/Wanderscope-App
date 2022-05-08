package cz.cvut.fit.steuejan.wanderscope.auth.intro

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentIntroBinding

class IntroFragment : MvvmFragment<FragmentIntroBinding, IntroFragmentVM>(
    R.layout.fragment_intro,
    IntroFragmentVM::class
) {
    override val hasBottomNavigation = false
    override val hasToolbar = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleLogin()
    }

    private fun handleLogin() {
        viewModel.shouldLogin.safeObserve { login ->
            if (login) {
                login()
            } else {
                goToHomeScreen()
            }
        }
    }

    private fun goToHomeScreen() {
        findNavController().popBackStack(R.id.introFragment, false)
        navigateTo(R.id.homeFragment)
    }
}