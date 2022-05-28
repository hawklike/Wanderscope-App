package cz.cvut.fit.steuejan.wanderscope.home

import android.os.Bundle
import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.MainActivityVM
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class HomeFragment : MvvmFragment<FragmentHomeBinding, HomeFragmentVM>(
    R.layout.fragment_home,
    HomeFragmentVM::class
), WithLoading {

    override val hasToolbar = false

    override val content: View get() = binding.homeContent
    override val shimmer: ShimmerFrameLayout get() = binding.homeShimmer

    private val mainVM by sharedViewModel<MainActivityVM>()

    private var init = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveRecommendedTrip()
        handleLogin()
        handleLoading()
    }

    private fun retrieveRecommendedTrip() {
        viewModel.getRecommendedTrip(getString(R.string.no_upcoming_trips_title), init)
        init = false
    }

    private fun handleLogin() {
        viewModel.shouldLogin.safeObserve { shouldLogin ->
            if (shouldLogin) {
                login()
            } else {
                mainVM.hideSplashScreen()
            }
        }
    }

    private fun handleLoading() {
        showLoading()
        viewModel.loading.safeObserve { loading ->
            if (!loading) {
                hideLoading()
            }
        }
    }
}