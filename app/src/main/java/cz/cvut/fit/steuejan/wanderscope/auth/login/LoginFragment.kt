package cz.cvut.fit.steuejan.wanderscope.auth.login

import android.os.Bundle
import android.view.View
import cz.cvut.fit.steuejan.wanderscope.MainActivityVM
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : MvvmFragment<FragmentLoginBinding, LoginFragmentVM>(
    R.layout.fragment_login,
    LoginFragmentVM::class
) {
    override val hasBottomNavigation: Boolean = false
    override val hasToolbar: Boolean = false

    private val mainVM by sharedViewModel<MainActivityVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainVM.hideSplashScreen()
    }

    override fun onStart() {
        super.onStart()
        setStatusBarColor(R.color.colorPrimary)
    }

    override fun onStop() {
        super.onStop()
        setStatusBarColor(R.color.colorBackground)
    }
}