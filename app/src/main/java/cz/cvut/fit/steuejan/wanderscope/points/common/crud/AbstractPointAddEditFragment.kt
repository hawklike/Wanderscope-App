package cz.cvut.fit.steuejan.wanderscope.points.common.crud

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import timber.log.Timber
import kotlin.reflect.KClass

abstract class AbstractPointAddEditFragment<B : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes layoutId: Int,
    viewModelClass: KClass<VM>
) : MvvmFragment<B, VM>(layoutId, viewModelClass) {

    override val hasBottomNavigation = false
    override val hasTitle = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    protected abstract fun initViewModel()

    @Suppress("DEPRECATION")
    protected open fun showPlacesAutocomplete(fields: List<Place.Field>, query: String?) {
        val intent = Autocomplete
            .IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .setInitialQuery(query)
            .build(requireContext())
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
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

    protected open fun placesSuccess(data: Intent?) {
        data?.let {
            val place = Autocomplete.getPlaceFromIntent(data)
            (viewModel as? AbstractPointAddEditFragmentVM<*>)?.placeFound(place)
        }
    }

    protected open fun placesError(data: Intent?) {
        data?.let {
            val status = Autocomplete.getStatusFromIntent(data)
            Timber.e(status.statusMessage)
        }
    }

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 1
    }
}