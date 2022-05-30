package cz.cvut.fit.steuejan.wanderscope.points.common.overview

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.SnackbarInfo
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.binding.visibleOrGone
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.extension.addMarker
import cz.cvut.fit.steuejan.wanderscope.app.extension.adjustZoom
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.bundle.PointOverviewBundle
import kotlin.reflect.KClass

abstract class AbstractPointOverviewFragment<B : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes layoutId: Int,
    viewModelClass: KClass<VM>
) : MvvmFragment<B, VM>(
    layoutId,
    viewModelClass
), WithLoading, WithRecycler {

    override val hasBottomNavigation = false
    override val hasTitle = false

    protected abstract val pointOverview: PointOverviewBundle

    protected abstract val map: MapView?

    protected abstract val menuEditItem: Int
    protected abstract val menuDeleteItem: Int

    protected abstract val addDocumentButton: MaterialTextView

    abstract fun setFragmentResult()

    private val abstractViewModel by lazy {
        viewModel as? AbstractPointOverviewFragmentVM<*>
    }

    protected abstract fun setTitle(title: String)

    protected abstract fun handleResponse(response: PointResponse)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(pointOverview.title)
        handleActionButtons()
        retrievePointOverview()
        waitUntilMapAndCoordinatesAreReady()
        prepareMap(savedInstanceState)
        goToWebsiteIntent()
        launchMapIntent()
        handleLoadingDelete()
        handleSuccessDelete()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(Menu.NONE, R.id.point_overview_edit, Menu.FIRST, menuEditItem)
        menu.add(Menu.NONE, R.id.point_overview_delete, Menu.FIRST + 1, menuDeleteItem)
        menu.add(Menu.NONE, R.id.point_overview_add_to_calendar, Menu.FIRST + 2, R.string.save_to_calendar)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        when (pointOverview.userRole) {
            UserRole.VIEWER -> {
                menu.findItem(R.id.point_overview_edit)?.isVisible = false
                menu.findItem(R.id.point_overview_delete)?.isVisible = false
            }
            else -> doNothing
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.point_overview_edit -> editPoint()
            R.id.point_overview_delete -> deletePoint()
            R.id.point_overview_add_to_calendar -> saveToCalendar()
            else -> super.onOptionsItemSelected(item)
        }
    }

    abstract fun editPoint(): Boolean
    abstract fun deletePoint(): Boolean
    abstract fun saveToCalendar(): Boolean

    private fun handleActionButtons() {
        addDocumentButton.visibleOrGone(pointOverview.userRole.canEdit())
    }

    private fun prepareMap(savedInstanceState: Bundle?) {
        val mapViewBundle = savedInstanceState?.getBundle(MAP_BUNDLE_KEY)
        map?.onCreate(mapViewBundle)
        map?.getMapAsync(::onMapReady)
    }

    private var deleteSnackbar: Snackbar? = null

    private fun handleLoadingDelete() {
        abstractViewModel?.deleteLoading?.safeObserve { title ->
            deleteSnackbar = showSnackbar(
                SnackbarInfo(
                    title,
                    length = Snackbar.LENGTH_INDEFINITE
                )
            )
        }
    }

    private fun handleSuccessDelete() {
        abstractViewModel?.deleteIsSuccess?.safeObserve {
            deleteSnackbar?.dismiss()
            updateTripPoint()
            setFragmentResult()
            navigateBack()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mapBundle = outState.getBundle(MAP_BUNDLE_KEY) ?: run {
            val bundle = Bundle()
            outState.putBundle(MAP_BUNDLE_KEY, bundle)
            bundle
        }
        map?.onSaveInstanceState(mapBundle)
    }

    override fun onResume() {
        super.onResume()
        map?.onResume()
    }

    override fun onStart() {
        super.onStart()
        map?.onStart()
    }

    override fun onStop() {
        super.onStop()
        map?.onStop()
    }

    override fun onPause() {
        super.onPause()
        map?.onPause()
    }

    override fun onDestroy() {
        map?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map?.onLowMemory()
    }

    protected fun pleaseWait(): Boolean {
        showToast(R.string.please_wait)
        return true
    }

    private fun retrievePointOverview() {
        showLoading()
        abstractViewModel?.getPoint(pointOverview.tripId, pointOverview.pointId)

        abstractViewModel?.loading?.safeObserve { loading ->
            if (!loading) {
                hideLoading()
            }
        }

        abstractViewModel?.pointOverview?.safeObserve {
            setTitle(it.name)
            handleResponse(it)
        }
    }

    protected open fun onMapReady(googleMap: GoogleMap) {
        abstractViewModel?.mapReady?.value = googleMap
    }

    protected open fun waitUntilMapAndCoordinatesAreReady() {
        abstractViewModel?.mapAndCoordinatesReady?.safeObserve { (googleMap, location) ->
            val marker = googleMap?.addMarker(location?.coordinates, location?.title)
            googleMap?.adjustZoom(map ?: return@safeObserve, marker)
        }
    }

    private fun goToWebsiteIntent() {
        abstractViewModel?.goToWebsite?.safeObserve(::startActivitySafe)
    }

    private fun launchMapIntent() {
        abstractViewModel?.launchMap?.safeObserve(::startActivitySafe)
    }

    companion object {
        const val MAP_BUNDLE_KEY = "ostrava je region razovity"
    }
}