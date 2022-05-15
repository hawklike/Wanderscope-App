@file:Suppress("DEPRECATION")

package cz.cvut.fit.steuejan.wanderscope.points.accommodation.crud

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointAccommodationAddEditBinding
import timber.log.Timber

class AccommodationAddEditFragment : MvvmFragment<
        FragmentPointAccommodationAddEditBinding,
        AccommodationAddEditFragmentVM>(
    R.layout.fragment_point_accommodation_add_edit,
    AccommodationAddEditFragmentVM::class
) {

    override val hasBottomNavigation = false

    private val args: AccommodationAddEditFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        findAccommodation()
    }

    private fun initViewModel() {
        viewModel.init(args.tripId, R.string.add_accommodation)
    }

    private fun findAccommodation() {
        viewModel.findAccommodationEvent.safeObserve {
            showPlacesAutocomplete(it)
        }
    }

    private fun showPlacesAutocomplete(query: String?) {
        val fields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.PHONE_NUMBER,
            Place.Field.WEBSITE_URI
        )
        val intent = Autocomplete
            .IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .setInitialQuery(query)
            .build(requireContext())
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> placesSuccess(data)
                AutocompleteActivity.RESULT_ERROR -> placesError(data)
                Activity.RESULT_CANCELED -> doNothing
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun placesSuccess(data: Intent?) {
        data?.let {
            val place = Autocomplete.getPlaceFromIntent(data)
            viewModel.placeFound(place)
        }
    }

    private fun placesError(data: Intent?) {
        data?.let {
            val status = Autocomplete.getStatusFromIntent(data)
            Timber.e(status.statusMessage)
        }
    }

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 1
    }
}