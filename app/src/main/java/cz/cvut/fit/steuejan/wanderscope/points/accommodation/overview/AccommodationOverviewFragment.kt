package cz.cvut.fit.steuejan.wanderscope.points.accommodation.overview

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.maps.MapView
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointAccommodationOverviewBinding
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.api.response.AccommodationResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragment
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragment

class AccommodationOverviewFragment : AbstractPointOverviewFragment<
        FragmentPointAccommodationOverviewBinding,
        AccommodationOverviewFragmentVM>(
    R.layout.fragment_point_accommodation_overview,
    AccommodationOverviewFragmentVM::class
) {
    override val content: View get() = binding.accommodationOverviewContent
    override val shimmer: ShimmerFrameLayout get() = binding.accommodationOverviewShimmer

    override val pointOverview by lazy { args.overviewBundle }

    override val map: MapView get() = binding.accommodationOverviewMap

    override val menuEditItem = R.string.edit_accommodation
    override val menuDeleteItem = R.string.delete_accommodation

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

    override fun editPoint(): Boolean {
        val accommodation = accommodationOverview ?: return pleaseWait()
        navigateTo(
            AccommodationOverviewFragmentDirections
                .actionAccommodationOverviewFragmentToAccommodationAddEditFragment(
                    pointOverview.tripId, accommodation
                )
        )
        return true
    }

    override fun deletePoint(): Boolean {
        viewModel.deleteAccommodation(pointOverview.tripId, pointOverview.pointId)
        return true
    }

    override fun saveToCalendar(): Boolean {
        //todo
        return true
    }

    override fun setFragmentResult() {
        setFragmentResult(
            TripPagerFragment.TRIP_OVERVIEW_REQUEST_KEY,
            bundleOf(TripPagerFragment.TRIP_OVERVIEW_RESULT_BUNDLE to Load.ACCOMMODATION)
        )
    }

    private fun phoneCallIntent() {
        viewModel.launchPhoneCall.safeObserve(::startActivitySafe)
    }

    private fun sendEmailIntent() {
        viewModel.sendEmail.safeObserve(::startActivitySafe)
    }
}