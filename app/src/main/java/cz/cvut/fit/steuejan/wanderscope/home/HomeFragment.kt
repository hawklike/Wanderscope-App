package cz.cvut.fit.steuejan.wanderscope.home

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentHomeBinding


class HomeFragment : MvvmFragment<FragmentHomeBinding, HomeFragmentVM>(
    R.layout.fragment_home,
    HomeFragmentVM::class
)