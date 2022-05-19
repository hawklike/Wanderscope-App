package cz.cvut.fit.steuejan.wanderscope.points.transport.overview

import android.view.View
import androidx.navigation.fragment.navArgs
import com.facebook.shimmer.ShimmerFrameLayout
import cz.cvut.fit.steuejan.wanderscope.R
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

    override val tripId by lazy { args.tripId }
    override val pointId by lazy { args.transportId }
    override val name by lazy { args.title }

    private val args: TransportOverviewFragmentArgs by navArgs()

    override fun handleResponse(response: PointResponse) {
        if (response !is TransportResponse) {
            return
        }
    }

    override fun setTitle(title: String) {
        binding.transportOverviewTitle.text = title
    }

}