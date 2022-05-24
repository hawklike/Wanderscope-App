package cz.cvut.fit.steuejan.wanderscope.points.accommodation.crud

import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointAccommodationAddEditBinding
import cz.cvut.fit.steuejan.wanderscope.points.accommodation.model.AccommodationType
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragment
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragment

class AccommodationAddEditFragment : AbstractPointAddEditFragment<
        FragmentPointAccommodationAddEditBinding,
        AccommodationAddEditFragmentVM>(
    R.layout.fragment_point_accommodation_add_edit,
    AccommodationAddEditFragmentVM::class
) {

    private val args: AccommodationAddEditFragmentArgs by navArgs()

    override fun initViewModel() {
        viewModel.init(args.tripId, R.string.add_accommodation)
        setupEditAccommodation()
    }

    private fun setupEditAccommodation() {
        args.response?.let {
            viewModel.setupEdit(it, R.string.edit_accommodation)
        }
    }

    override fun prepareDropdownItems(): List<String> {
        return AccommodationType.values().map {
            getString(it.toStringRes())
        }
    }

    override val dropdownView: AutoCompleteTextView?
        get() = (binding.addAccommodationType.editText as? AutoCompleteTextView)

    override val fields = listOf(
        Place.Field.ID,
        Place.Field.NAME,
        Place.Field.ADDRESS,
        Place.Field.PHONE_NUMBER,
        Place.Field.WEBSITE_URI,
        Place.Field.LAT_LNG
    )

    override fun setFragmentResult() {
        setFragmentResult(
            TripPagerFragment.TRIP_OVERVIEW_REQUEST_KEY,
            bundleOf(TripPagerFragment.TRIP_OVERVIEW_RESULT_BUNDLE to Load.ACCOMMODATION)
        )
    }
}