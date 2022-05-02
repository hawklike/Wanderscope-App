package cz.cvut.fit.steuejan.wanderscope.auth.register

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentRegisterBinding

class RegisterFragment :
    MvvmFragment<FragmentRegisterBinding, RegisterVM>(R.layout.fragment_register, RegisterVM::class) {
}