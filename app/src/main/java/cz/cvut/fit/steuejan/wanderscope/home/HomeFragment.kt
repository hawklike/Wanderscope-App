package cz.cvut.fit.steuejan.wanderscope.home

import android.os.Bundle
import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.MainActivityVM
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentHomeBinding
import cz.cvut.fit.steuejan.wanderscope.trips.api.response.TripOverviewResponse
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class HomeFragment : MvvmFragment<FragmentHomeBinding, HomeFragmentVM>(
    R.layout.fragment_home,
    HomeFragmentVM::class
), WithLoading, WithRecycler {

    override val hasToolbar = false

    override val content: View get() = binding.homeContent
    override val shimmer: ShimmerFrameLayout get() = binding.homeShimmer

    private val mainVM by sharedViewModel<MainActivityVM>()

    private var init = true
    private var initScrolling = true

    private var tripOverview: TripOverviewResponse? = null

    private val tripEmptyTitle by lazy {
        getString(R.string.no_upcoming_trips_title)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getRecommendedTrip(tripEmptyTitle, init)
        init = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleLogin()
        handleLoading()
        updateTripOverview()
        handleRecyclerScrolling()
        listenToChanges()
    }

    private fun listenToChanges() {
        mainVM.updateTrip.safeObserve { update ->
            if (update) {
                viewModel.getRecommendedTrip(tripEmptyTitle, init)
            }
        }
        mainVM.updateTripPoint.safeObserve { update ->
            if (update) {
                viewModel.getItinerary(tripOverview?.id ?: return@safeObserve)
            }
        }
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

    private fun handleRecyclerScrolling() {
        viewModel.activeItemIdx.safeObserve {
            if (initScrolling) {
                scrollToPosition(binding.homeItinerary, it)
                initScrolling = false
            }
        }
    }

    private fun updateTripOverview() {
        viewModel.tripOverview.safeObserve {
            tripOverview = it
        }
    }
}