package cz.cvut.fit.steuejan.wanderscope.points.activity.crud

import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointActivityAddEditBinding
import cz.cvut.fit.steuejan.wanderscope.points.activity.model.ActivityType
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragment
import cz.cvut.fit.steuejan.wanderscope.trip.model.Load
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragment

class ActivityAddEditFragment : AbstractPointAddEditFragment<
        FragmentPointActivityAddEditBinding,
        ActivityAddEditFragmentVM>(
    R.layout.fragment_point_activity_add_edit,
    ActivityAddEditFragmentVM::class
) {

    private val args: ActivityAddEditFragmentArgs by navArgs()

    override fun initViewModel() {
        viewModel.init(args.tripId, R.string.add_activity)
        setupEditActivity()
    }

    private fun setupEditActivity() {
        args.response?.let {
            viewModel.setupEdit(it, R.string.edit_activity)
        }
    }

    override fun prepareDropdownItems(): List<String> {
        return ActivityType.values().map {
            getString(it.toStringRes())
        }
    }

    override val dropdownView: AutoCompleteTextView?
        get() = (binding.addActivityType.editText as? AutoCompleteTextView)

    override val fields = listOf(
        Place.Field.ID,
        Place.Field.NAME,
        Place.Field.ADDRESS,
        Place.Field.WEBSITE_URI,
        Place.Field.LAT_LNG
    )

    override fun setFragmentResult() {
        setFragmentResult(
            TripPagerFragment.TRIP_UPDATED_REQUEST_KEY,
            bundleOf(TripPagerFragment.TRIP_UPDATED_RESULT_BUNDLE to Load.ACTIVITIES)
        )
    }
}