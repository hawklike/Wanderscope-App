package cz.cvut.fit.steuejan.wanderscope.account.change_password

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentChangePasswordBinding

class ChangePasswordFragment : MvvmFragment<
        FragmentChangePasswordBinding,
        ChangePasswordFragmentVM>(
    R.layout.fragment_change_password,
    ChangePasswordFragmentVM::class
) {
    override val hasBottomNavigation = false
    override val hasTitle = false
}