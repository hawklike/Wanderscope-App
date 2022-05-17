package cz.cvut.fit.steuejan.wanderscope.points.place.crud

import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.navArgs
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointPlaceAddEditBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragment
import cz.cvut.fit.steuejan.wanderscope.points.place.model.PlaceType

class PlaceAddEditFragment : AbstractPointAddEditFragment<
        FragmentPointPlaceAddEditBinding,
        PlaceAddEditFragmentVM>(
    R.layout.fragment_point_place_add_edit,
    PlaceAddEditFragmentVM::class
) {

    private val args: PlaceAddEditFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPlace()
    }

    override fun initViewModel() {
        viewModel.init(args.tripId, R.string.add_place)
    }

    override fun prepareDropdownItems(): List<String> {
        return PlaceType.values().map {
            getString(it.toStringRes())
        }
    }

    override val dropdownView: AutoCompleteTextView?
        get() = (binding.addPlaceType.editText as? AutoCompleteTextView)

    private fun findPlace() {
        val fields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.WEBSITE_URI,
            Place.Field.LAT_LNG
        )
        viewModel.findAccommodationEvent.safeObserve {
            showPlacesAutocomplete(fields, it)
        }
    }
}