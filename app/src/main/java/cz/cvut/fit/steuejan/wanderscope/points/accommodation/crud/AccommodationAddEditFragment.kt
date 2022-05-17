package cz.cvut.fit.steuejan.wanderscope.points.accommodation.crud

import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.navArgs
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointAccommodationAddEditBinding
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.model.AccommodationType
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragment

class AccommodationAddEditFragment : AbstractPointAddEditFragment<
        FragmentPointAccommodationAddEditBinding,
        AccommodationAddEditFragmentVM>(
    R.layout.fragment_point_accommodation_add_edit,
    AccommodationAddEditFragmentVM::class
) {

    private val args: AccommodationAddEditFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findAccommodationPlace()
    }

    override fun initViewModel() {
        viewModel.init(args.tripId, R.string.add_accommodation)
    }

    override fun prepareDropdownItems(): List<String> {
        return AccommodationType.values().map {
            getString(it.toStringRes())
        }
    }

    override val dropdownView: AutoCompleteTextView?
        get() = (binding.addAccommodationType.editText as? AutoCompleteTextView)

    private fun findAccommodationPlace() {
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
}