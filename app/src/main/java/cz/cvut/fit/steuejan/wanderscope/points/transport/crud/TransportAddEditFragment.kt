package cz.cvut.fit.steuejan.wanderscope.points.transport.crud

import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.navArgs
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointTransportAddEditBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragment
import cz.cvut.fit.steuejan.wanderscope.points.transport.model.TransportType

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

    override fun prepareDropdownItems(): List<String> {
        return TransportType.values().map {
            getString(it.toStringRes())
        }
    }

    override val dropdownView: AutoCompleteTextView?
        get() = (binding.addTransportType.editText as? AutoCompleteTextView)

    override val fields = listOf(
        Place.Field.ID,
        Place.Field.NAME,
        Place.Field.ADDRESS
    )
}