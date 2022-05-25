package cz.cvut.fit.steuejan.wanderscope.trip.overview.root

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.arch.viewpager.WithViewPager
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripPagerBinding
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.TripOverviewFragment
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.TripExpensesFragment
import cz.cvut.fit.steuejan.wanderscope.trip.overview.itinerary.TripItineraryFragment

class TripPagerFragment : MvvmFragment<FragmentTripPagerBinding, TripPagerFragmentVM>(
    R.layout.fragment_trip_pager,
    TripPagerFragmentVM::class
), WithViewPager {

    override val hasBottomNavigation: Boolean by lazy { args.hasBottomNavigation }

    private val args: TripPagerFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFragmentResultListener()
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener(TRIP_UPDATED_REQUEST_KEY) { _, bundle ->
            val result = bundle.getParcelable<Load>(TRIP_UPDATED_RESULT_BUNDLE)
            viewModel.tripOverviewResult.value = result
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tripPagerTitle.text = args.title

        val viewPager = binding.tripPagerViewPager
        val tabLayout = binding.tripPagerTabLayout

        val adapter = TripPagerAdapter(this, args.id, args.userRole)

        viewPager.apply {
            isUserInputEnabled = false
            this.adapter = adapter
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()
    }

    class TripPagerAdapter(
        private val fragment: Fragment,
        private val tripId: Int,
        private val userRole: UserRole
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TripItineraryFragment.newInstance(tripId, userRole)
                1 -> TripOverviewFragment.newInstance(tripId, userRole)
                2 -> TripExpensesFragment.newInstance()
                else -> TripItineraryFragment.newInstance(tripId, userRole)
            }
        }

        fun getTitle(position: Int): String {
            return when (position) {
                0 -> fragment.getString(R.string.trip_overview_itinerary)
                1 -> fragment.getString(R.string.trip_overview_overview)
                2 -> fragment.getString(R.string.trip_overview_expenses)
                else -> fragment.getString(R.string.trip_overview_overview)
            }
        }
    }

    override fun setTitle(title: String?) {
        binding.tripPagerTitle.text = title
    }

    companion object {
        const val TRIP_UPDATED_REQUEST_KEY = "tripUpdatedRequestKey"
        const val TRIP_UPDATED_RESULT_BUNDLE = "tripUpdatedResultBundle"
    }
}