package cz.cvut.fit.steuejan.wanderscope.home

import android.os.Bundle
import android.view.View
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentHomeBinding


class HomeFragment : MvvmFragment<FragmentHomeBinding, HomeFragmentVM>(
    R.layout.fragment_home,
    HomeFragmentVM::class
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.logout.safeObserve {
            logout()
        }
    }
}