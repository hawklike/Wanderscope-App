package cz.cvut.fit.steuejan.wanderscope.points.common.crud

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import timber.log.Timber
import kotlin.reflect.KClass

abstract class AbstractPointAddEditFragment<B : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes layoutId: Int,
    viewModelClass: KClass<VM>
) : MvvmFragment<B, VM>(layoutId, viewModelClass) {

    override val hasBottomNavigation = false
    override val hasTitle = false

    protected abstract fun initViewModel()

    abstract fun prepareDropdownItems(): List<String>
    abstract val dropdownView: AutoCompleteTextView?
    abstract val fields: List<Place.Field>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        prepareDropdownMenu()
        findPlace()
        hideKeyboard()
    }

    protected open fun findPlace() {
        (viewModel as? AbstractPointAddEditFragmentVM<*>)
            ?.findAccommodationEvent?.safeObserve {
                showPlacesAutocomplete(fields, it)
            }
    }

    override fun hideKeyboard() {
        (viewModel as? AbstractPointAddEditFragmentVM<*>)
            ?.hideKeyboardEvent?.safeObserve {
                super.hideKeyboard()
            }
    }

    protected open fun prepareDropdownMenu() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val items = withDefault { prepareDropdownItems() }
            val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_menu, items)
            dropdownView?.setAdapter(adapter)
        }
    }

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