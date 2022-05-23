package cz.cvut.fit.steuejan.wanderscope.points.transport.overview

import android.view.View
import androidx.navigation.fragment.navArgs
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.maps.MapView
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.extension.addMarker
import cz.cvut.fit.steuejan.wanderscope.app.extension.adjustZoom
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointTransportOverviewBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragment
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.response.TransportResponse

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
        //todo
        return true
    }

    override fun saveToCalendar(): Boolean {
        //todo
        return true
    }

    override fun waitUntilMapAndCoordinatesAreReady() {
        viewModel.mapAndAllCoordinatesReady.safeObserve { (googleMap, from, to) ->
            val markerFrom = googleMap?.addMarker(from?.coordinates, from?.title)
            val markerTo = googleMap?.addMarker(to?.coordinates, to?.title)
            googleMap?.adjustZoom(map, markerFrom, markerTo)
        }
    }
}