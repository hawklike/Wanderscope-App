package cz.cvut.fit.steuejan.wanderscope.auth.login

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentLoginBinding

class LoginFragment : MvvmFragment<FragmentLoginBinding, LoginVM>(R.layout.fragment_login, LoginVM::class) {
}