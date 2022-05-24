package cz.cvut.fit.steuejan.wanderscope.points.activity.overview

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.maps.MapView
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.util.saveEventToCalendar
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointActivityOverviewBinding
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.response.ActivityResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragment
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragment

class ActivityOverviewFragment : AbstractPointOverviewFragment<
        FragmentPointActivityOverviewBinding,
        ActivityOverviewFragmentVM>(
    R.layout.fragment_point_activity_overview,
    ActivityOverviewFragmentVM::class
) {
    override val content: View get() = binding.activityOverviewContent
    override val shimmer: ShimmerFrameLayout get() = binding.activityOverviewShimmer

    override val pointOverview by lazy { args.overviewBundle }

    override val map: MapView get() = binding.activityOverviewMap

    override val menuEditItem = R.string.edit_activity
    override val menuDeleteItem = R.string.delete_activity

    private val args: ActivityOverviewFragmentArgs by navArgs()

    private var activityOverview: ActivityResponse? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapLinkIntent()
    }

    override fun setTitle(title: String) {
        binding.activityOverviewTitle.text = title
    }

    override fun handleResponse(response: PointResponse) {
        activityOverview = response as? ActivityResponse
    }

    override fun editPoint(): Boolean {
        val activity = activityOverview ?: return pleaseWait()
        navigateTo(
            ActivityOverviewFragmentDirections
                .actionActivityOverviewFragmentToActivityAddEditFragment(
                    pointOverview.tripId, activity
                )
        )
        return true
    }

    override fun deletePoint(): Boolean {
        viewModel.deleteActivity(pointOverview.tripId, pointOverview.pointId)
        return true
    }

    override fun saveToCalendar(): Boolean {
        activityOverview?.let {
            with(it) {
                startActivitySafe(
                    saveEventToCalendar(
                        duration.startDate,
                        duration.endDate,
                        allDay = false,
                        name,
                        description,
                        address.name
                    )
                )
            }
        }
        return true
    }

    override fun setFragmentResult() {
        setFragmentResult(
            TripPagerFragment.TRIP_OVERVIEW_REQUEST_KEY,
            bundleOf(TripPagerFragment.TRIP_OVERVIEW_RESULT_BUNDLE to Load.ACTIVITIES)
        )
    }

    private fun mapLinkIntent() {
        viewModel.launchMapLink.safeObserve(::startActivitySafe)
    }
}