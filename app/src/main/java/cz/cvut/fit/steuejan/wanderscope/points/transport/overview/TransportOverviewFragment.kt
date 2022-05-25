package cz.cvut.fit.steuejan.wanderscope.points.transport.overview

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.maps.MapView
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.extension.addMarker
import cz.cvut.fit.steuejan.wanderscope.app.extension.adjustZoom
import cz.cvut.fit.steuejan.wanderscope.app.util.saveEventToCalendar
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointTransportOverviewBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragment
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.response.TransportResponse
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragment

class TransportOverviewFragment : AbstractPointOverviewFragment<
        FragmentPointTransportOverviewBinding,
        TransportOverviewFragmentVM>(
    R.layout.fragment_point_transport_overview,
    TransportOverviewFragmentVM::class
) {

    override val content: View get() = binding.transportOverviewContent
    override val shimmer: ShimmerFrameLayout get() = binding.transportOverviewShimmer

    override val pointOverview by lazy { args.overviewBundle }

    override val map: MapView get() = binding.transportOverviewMap

    override val menuEditItem = R.string.edit_transport
    override val menuDeleteItem = R.string.delete_transport

    private val args: TransportOverviewFragmentArgs by navArgs()

    private var transportOverview: TransportResponse? = null

    override fun setTitle(title: String) {
        binding.transportOverviewTitle.text = title
    }

    override fun handleResponse(response: PointResponse) {
        transportOverview = response as? TransportResponse
    }

    override fun editPoint(): Boolean {
        val transport = transportOverview ?: return pleaseWait()
        navigateTo(
            TransportOverviewFragmentDirections
                .actionTransportOverviewFragmentToTransportAddEditFragment(
                    pointOverview.tripId, transport
                )
        )
        return true
    }

    override fun deletePoint(): Boolean {
        viewModel.deleteTransport(pointOverview.tripId, pointOverview.pointId)
        return true
    }

    override fun saveToCalendar(): Boolean {
        transportOverview?.let {
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
            bundleOf(TripPagerFragment.TRIP_UPDATED_RESULT_BUNDLE to Load.TRANSPORT)
        )
    }

    override fun waitUntilMapAndCoordinatesAreReady() {
        viewModel.mapAndAllCoordinatesReady.safeObserve { (googleMap, from, to) ->
            val markerFrom = googleMap?.addMarker(from?.coordinates, from?.title)
            val markerTo = googleMap?.addMarker(to?.coordinates, to?.title)
            googleMap?.adjustZoom(map, markerFrom, markerTo)
        }
    }
}