package cz.cvut.fit.steuejan.wanderscope.auth.login

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentLoginBinding

class LoginFragment : MvvmFragment<FragmentLoginBinding, LoginFragmentVM>(
    R.layout.fragment_login,
    LoginFragmentVM::class
) {
    override val hasBottomNavigation: Boolean = false
    override val hasToolbar: Boolean = false

    override fun onStart() {
        super.onStart()
        setStatusBarColor(R.color.colorPrimary)
    }

    override fun onStop() {
        super.onStop()
        setStatusBarColor(R.color.colorBackground)
    }
}