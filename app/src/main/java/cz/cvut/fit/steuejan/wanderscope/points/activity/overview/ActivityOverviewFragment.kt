package cz.cvut.fit.steuejan.wanderscope.points.activity.overview

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.maps.MapView
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointActivityOverviewBinding
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.response.ActivityResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragment

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
        //todo
        return true
    }

    override fun deletePoint(): Boolean {
        //todo
        return true
    }

    override fun saveToCalendar(): Boolean {
        //todo
        return true
    }

    private fun mapLinkIntent() {
        viewModel.launchMapLink.safeObserve(::startActivitySafe)
    }
}