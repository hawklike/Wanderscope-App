package cz.cvut.fit.steuejan.wanderscope.points.transport.crud

import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.common.WithChipGroup
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointTransportAddEditBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragment
import cz.cvut.fit.steuejan.wanderscope.points.transport.model.TransportType

class TransportAddEditFragment : AbstractPointAddEditFragment<
        FragmentPointTransportAddEditBinding,
        TransportAddEditFragmentVM>(
    R.layout.fragment_point_transport_add_edit,
    TransportAddEditFragmentVM::class
), WithChipGroup {

    private val args: TransportAddEditFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleSubmit()
    }

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
        Place.Field.ADDRESS,
        Place.Field.LAT_LNG
    )

    private fun handleSubmit() {
        viewModel.submitEvent.safeObserve {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                val cars = extractChips(binding.addTransportCarsGroup).takeIf { it.isNotEmpty() }
                val seats = extractChips(binding.addTransportSeatsGroup).takeIf { it.isNotEmpty() }
                val request = it.copy(cars = cars, seats = seats)
                viewModel.submit(request)
            }
        }
    }
}