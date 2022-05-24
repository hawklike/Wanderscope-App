package cz.cvut.fit.steuejan.wanderscope.points.place.crud

import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointPlaceAddEditBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragment
import cz.cvut.fit.steuejan.wanderscope.points.place.model.PlaceType
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragment

class PlaceAddEditFragment : AbstractPointAddEditFragment<
        FragmentPointPlaceAddEditBinding,
        PlaceAddEditFragmentVM>(
    R.layout.fragment_point_place_add_edit,
    PlaceAddEditFragmentVM::class
) {

    private val args: PlaceAddEditFragmentArgs by navArgs()

    override fun initViewModel() {
        viewModel.init(args.tripId, R.string.add_place)
        setupEditPlace()
    }

    private fun setupEditPlace() {
        args.response?.let {
            viewModel.setupEdit(it, R.string.edit_place)
        }
    }

    override fun prepareDropdownItems(): List<String> {
        return PlaceType.values().map {
            getString(it.toStringRes())
        }
    }

    override val dropdownView: AutoCompleteTextView?
        get() = (binding.addPlaceType.editText as? AutoCompleteTextView)

    override val fields = listOf(
        Place.Field.ID,
        Place.Field.NAME,
        Place.Field.ADDRESS,
        Place.Field.WEBSITE_URI,
        Place.Field.LAT_LNG
    )

    override fun setFragmentResult() {
        setFragmentResult(
            TripPagerFragment.TRIP_OVERVIEW_REQUEST_KEY,
            bundleOf(TripPagerFragment.TRIP_OVERVIEW_RESULT_BUNDLE to Load.PLACES)
        )
    }
}