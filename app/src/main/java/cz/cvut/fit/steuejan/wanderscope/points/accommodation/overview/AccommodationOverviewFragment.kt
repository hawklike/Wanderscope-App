package cz.cvut.fit.steuejan.wanderscope.points.accommodation.overview

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.maps.MapView
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointAccommodationOverviewBinding
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.response.AccommodationResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragment

class AccommodationOverviewFragment : AbstractPointOverviewFragment<
        FragmentPointAccommodationOverviewBinding,
        AccommodationOverviewFragmentVM>(
    R.layout.fragment_point_accommodation_overview,
    AccommodationOverviewFragmentVM::class
) {
    override val content: View get() = binding.accommodationOverviewContent
    override val shimmer: ShimmerFrameLayout get() = binding.accommodationOverviewShimmer

    override val tripId by lazy { args.tripId }
    override val pointId by lazy { args.accommodationId }
    override val name by lazy { args.title }

    override val map: MapView get() = binding.accommodationOverviewMap

    private val args: AccommodationOverviewFragmentArgs by navArgs()

    private var accommodationOverview: AccommodationResponse? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phoneCallIntent()
        sendEmailIntent()
    }

    override fun setTitle(title: String) {
        binding.accommodationOverviewTitle.text = title
    }

    override fun handleResponse(response: PointResponse) {
        accommodationOverview = response as? AccommodationResponse
    }

    private fun phoneCallIntent() {
        viewModel.launchPhoneCall.safeObserve(::startActivitySafe)
    }

    private fun sendEmailIntent() {
        viewModel.sendEmail.safeObserve(::startActivitySafe)
    }
}