@file:Suppress("DEPRECATION")

package cz.cvut.fit.steuejan.wanderscope.points.accommodation.crud

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointAccommodationAddEditBinding
import timber.log.Timber

class AccommodationAddEditFragment : MvvmFragment<
        FragmentPointAccommodationAddEditBinding,
        AccommodationAddEditFragmentVM>(
    R.layout.fragment_point_accommodation_add_edit,
    AccommodationAddEditFragmentVM::class
) {

    override val hasBottomNavigation = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findAccommodation()
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
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        viewModel.placeFound(place)
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Timber.e(status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 1
    }
}