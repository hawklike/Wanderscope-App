package cz.cvut.fit.steuejan.wanderscope

import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentFirstBinding


class FirstFragment : MvvmFragment<FragmentFirstBinding, FirstFragmentVM>(
    R.layout.fragment_first,
    FirstFragmentVM::class
)