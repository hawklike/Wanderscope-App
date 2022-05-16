package cz.cvut.fit.steuejan.wanderscope.points.accommodation.crud

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.extension.capitalize
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointAccommodationAddEditBinding
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.model.AccommodationType
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragment
import kotlinx.coroutines.launch
import java.util.*

class AccommodationAddEditFragment : AbstractPointAddEditFragment<
        FragmentPointAccommodationAddEditBinding,
        AccommodationAddEditFragmentVM>(
    R.layout.fragment_point_accommodation_add_edit,
    AccommodationAddEditFragmentVM::class
) {

    private val args: AccommodationAddEditFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findAccommodation()
        prepareDropdownMenu()
    }

    override fun initViewModel() {
        viewModel.init(args.tripId, R.string.add_accommodation)
    }

    private fun findAccommodation() {
        val fields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.PHONE_NUMBER,
            Place.Field.WEBSITE_URI
        )
        viewModel.findAccommodationEvent.safeObserve {
            showPlacesAutocomplete(fields, it)
        }
    }

    private fun prepareDropdownMenu() {
        viewLifecycleOwner.lifecycleScope.launch {
            val items = withDefault {
                AccommodationType.values().map {
                    it.toString().lowercase(Locale.getDefault()).capitalize()
                }
            }
            val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_menu, items)
            (binding.addAccommodationType.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
    }
}