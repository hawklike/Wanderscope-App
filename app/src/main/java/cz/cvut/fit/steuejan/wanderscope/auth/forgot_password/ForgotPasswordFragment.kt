package cz.cvut.fit.steuejan.wanderscope.auth.forgot_password

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : MvvmFragment<FragmentForgotPasswordBinding, ForgotPasswordFragmentVM>(
    R.layout.fragment_forgot_password,
    ForgotPasswordFragmentVM::class
) {
    override val hasBottomNavigation = false
    override val hasTitle = false
}