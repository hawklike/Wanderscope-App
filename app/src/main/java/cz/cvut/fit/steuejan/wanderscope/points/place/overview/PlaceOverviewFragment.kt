package cz.cvut.fit.steuejan.wanderscope.points.place.overview

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.maps.MapView
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointPlaceOverviewBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragment
import cz.cvut.fit.steuejan.wanderscope.points.place.api.response.PlaceResponse

class PlaceOverviewFragment : AbstractPointOverviewFragment<
        FragmentPointPlaceOverviewBinding,
        PlaceOverviewFragmentVM>(
    R.layout.fragment_point_place_overview,
    PlaceOverviewFragmentVM::class
) {
    override val content: View get() = binding.placeOverviewContent
    override val shimmer: ShimmerFrameLayout get() = binding.placeOverviewShimmer

    override val tripId by lazy { args.tripId }
    override val pointId by lazy { args.placeId }
    override val name by lazy { args.title }

    override val map: MapView get() = binding.placeOverviewMap

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

    private fun showMapIntent() {
        viewModel.launchMapLatLon.safeObserve(::startActivitySafe)
    }
}