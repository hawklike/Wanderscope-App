package cz.cvut.fit.steuejan.wanderscope.trip.overview

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.viewpager.ViewPagerFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.app.util.saveEventToCalendar
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentTripOverviewBinding
import cz.cvut.fit.steuejan.wanderscope.trip.api.response.TripResponse
import cz.cvut.fit.steuejan.wanderscope.trip.crud.bundle.EditTripBundle
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentDirections

class TripOverviewFragment : ViewPagerFragment<FragmentTripOverviewBinding, TripOverviewFragmentVM>(
    R.layout.fragment_trip_overview,
    TripOverviewFragmentVM::class
), WithLoading {

    override val content: View get() = binding.tripOverviewContent
    override val shimmer: ShimmerFrameLayout get() = binding.tripOverviewShimmer

    private var tripOverview: TripResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        retrieveTripOverview()
    }

    private fun foo() {
        binding.tripOverviewAccommodation.adapter?.itemCount
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.trip_overview_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        when (tripOverview?.userRole) {
            UserRole.ADMIN -> {
                doNothing
            }
            UserRole.EDITOR -> {
                menu.findItem(R.id.action_trip_delete)?.isVisible = false
            }
            UserRole.VIEWER -> {
                menu.findItem(R.id.action_trip_delete)?.isVisible = false
                menu.findItem(R.id.action_trip_edit)?.isVisible = false
            }
            null -> doNothing
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_trip_edit -> editTrip()
            R.id.action_trip_delete -> deleteTrip()
            R.id.action_trip_save_to_calendar -> saveToCalendar()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun editTrip(): Boolean {
        val trip = tripOverview ?: return pleaseWait()
        val arg = EditTripBundle(
            trip.id,
            trip.name,
            trip.duration.startDate,
            trip.duration.endDate,
            trip.description
        )
        navigateTo(TripPagerFragmentDirections.actionTripPagerFragmentToAddTripFragment(arg))
        return true
    }

    private fun deleteTrip(): Boolean {
        val trip = tripOverview ?: return pleaseWait()
        return true
    }

    private fun saveToCalendar(): Boolean {
        val trip = tripOverview ?: return pleaseWait()
        with(trip) {
            startActivity(
                saveEventToCalendar(
                    duration.startDate,
                    duration.endDate,
                    allDay = true,
                    name,
                    description
                )
            )
        }
        return true
    }

    private fun pleaseWait(): Boolean {
        Toast.makeText(requireContext(), R.string.please_wait, Toast.LENGTH_SHORT).show()
        return true
    }

    private fun setTitle() {
        viewModel.title.safeObserve {
            setTitle(it)
        }
    }

    private fun retrieveTripOverview() {
        showLoading()
        viewModel.getTrip(arguments?.getInt(TRIP_ID) ?: return)

        viewModel.loading.safeObserve { loading ->
            if (!loading) {
                hideLoading()
            }
        }

        viewModel.tripOverview.safeObserve {
            tripOverview = it
        }
    }

    companion object {
        fun newInstance(tripId: Int): TripOverviewFragment {
            return TripOverviewFragment().apply {
                arguments = Bundle().apply {
                    putInt(TRIP_ID, tripId)
                }
            }
        }

        private const val TRIP_ID = "tripId"
    }

}