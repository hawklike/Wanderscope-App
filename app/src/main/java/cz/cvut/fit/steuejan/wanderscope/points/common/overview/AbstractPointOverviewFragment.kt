package cz.cvut.fit.steuejan.wanderscope.points.common.overview

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.app.extension.addMarker
import cz.cvut.fit.steuejan.wanderscope.app.extension.adjustZoom
import cz.cvut.fit.steuejan.wanderscope.app.util.goToWebsite
import cz.cvut.fit.steuejan.wanderscope.app.util.runOrLogException
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
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

    protected abstract val tripId: Int
    protected abstract val pointId: Int
    protected abstract val name: String

    protected abstract val map: MapView?

    private val abstractViewModel by lazy {
        viewModel as? AbstractPointOverviewFragmentVM<*>
    }

    protected abstract fun setTitle(title: String)

    protected abstract fun handleResponse(response: PointResponse)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(name)
        retrievePointOverview()
        waitUntilMapAndCoordinatesAreReady()
        prepareMap(savedInstanceState)
        handleGoToWebsite()
    }

    private fun prepareMap(savedInstanceState: Bundle?) {
        val mapViewBundle = savedInstanceState?.getBundle(MAP_BUNDLE_KEY)
        map?.onCreate(mapViewBundle)
        map?.getMapAsync(::onMapReady)
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


    private fun retrievePointOverview() {
        showLoading()
        abstractViewModel?.getPoint(tripId, pointId)

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

    private fun handleGoToWebsite() {
        abstractViewModel?.goToWebsite?.safeObserve {
            runOrLogException {
                startActivity(goToWebsite(it))
            }
        }
    }

    companion object {
        const val MAP_BUNDLE_KEY = "ostrava je region razovity"
    }
}