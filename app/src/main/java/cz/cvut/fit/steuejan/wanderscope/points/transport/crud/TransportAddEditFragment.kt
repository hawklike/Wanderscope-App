package cz.cvut.fit.steuejan.wanderscope.points.transport.crud

import androidx.navigation.fragment.navArgs
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointTransportAddEditBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragment

class TransportAddEditFragment : AbstractPointAddEditFragment<
        FragmentPointTransportAddEditBinding,
        TransportAddEditFragmentVM>(
    R.layout.fragment_point_transport_add_edit,
    TransportAddEditFragmentVM::class
) {

    private val args: TransportAddEditFragmentArgs by navArgs()

    override fun initViewModel() {
        viewModel.init(args.tripId, R.string.add_transport)
    }
}