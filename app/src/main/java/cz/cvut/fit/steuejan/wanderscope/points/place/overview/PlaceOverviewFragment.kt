package cz.cvut.fit.steuejan.wanderscope.points.place.overview

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.maps.MapView
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.util.saveEventToCalendar
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointPlaceOverviewBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragment
import cz.cvut.fit.steuejan.wanderscope.points.place.api.response.PlaceResponse
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragment

class PlaceOverviewFragment : AbstractPointOverviewFragment<
        FragmentPointPlaceOverviewBinding,
        PlaceOverviewFragmentVM>(
    R.layout.fragment_point_place_overview,
    PlaceOverviewFragmentVM::class
) {
    override val content: View get() = binding.placeOverviewContent
    override val shimmer: ShimmerFrameLayout get() = binding.placeOverviewShimmer

    override val pointOverview by lazy { args.overviewBundle }

    override val map: MapView get() = binding.placeOverviewMap

    override val menuEditItem = R.string.edit_place
    override val menuDeleteItem = R.string.delete_place

    private val args: PlaceOverviewFragmentArgs by navArgs()

    private var placeOverview: PlaceResponse? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMapIntent()
    }

    override fun setTitle(title: String) {
        binding.placeOverviewTitle.text = title
    }

    override fun handleResponse(response: PointResponse) {
        placeOverview = response as? PlaceResponse
    }

    override fun editPoint(): Boolean {
        val place = placeOverview ?: return pleaseWait()
        navigateTo(
            PlaceOverviewFragmentDirections
                .actionPlaceOverviewFragmentToPlaceAddEditFragment(
                    pointOverview.tripId, place
                )
        )
        return true
    }

    override fun deletePoint(): Boolean {
        viewModel.deletePlace(pointOverview.tripId, pointOverview.pointId)
        return true
    }

    override fun saveToCalendar(): Boolean {
        placeOverview?.let {
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
            TripPagerFragment.TRIP_UPDATED_REQUEST_KEY,
            bundleOf(TripPagerFragment.TRIP_UPDATED_RESULT_BUNDLE to Load.PLACES)
        )
    }

    private fun showMapIntent() {
        viewModel.launchMapLatLon.safeObserve(::startActivitySafe)
    }
}