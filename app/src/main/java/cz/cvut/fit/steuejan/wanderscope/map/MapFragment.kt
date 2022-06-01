package cz.cvut.fit.steuejan.wanderscope.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.common.map.LatLngBundle
import cz.cvut.fit.steuejan.wanderscope.app.common.map.WithMap
import cz.cvut.fit.steuejan.wanderscope.app.extension.addMarker
import cz.cvut.fit.steuejan.wanderscope.app.extension.adjustZoom
import cz.cvut.fit.steuejan.wanderscope.app.extension.zoomToCurrentLocation
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentMapBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType


class MapFragment : MvvmFragment<FragmentMapBinding, MapFragmentVM>(
    R.layout.fragment_map,
    MapFragmentVM::class
), WithMap {

    override val hasTitle = false

    override val hasBottomNavigation by lazy {
        args.hasBottomNavigation
    }

    override val map: MapView get() = binding.mapMap

    private val args: MapFragmentArgs by navArgs()

    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(args.title, args.duration)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCoordinates()
    }

    private fun getCoordinates() {
        getSharedData<List<LatLngBundle>>().safeObserve {
            it?.let {
                viewModel.saveCoordinates(it)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        enableMyLocation()
        viewModel.coordinates.safeObserve { coordinates ->
            var marker: Marker? = null
            coordinates?.forEach {
                marker = googleMap.addMarker(it.latLng, it.title, it.pointType.toHue())
            }
            if (!googleMap.zoomToCurrentLocation(requireContext(), CURRENT_LOCATION_ZOOM)) {
                googleMap.adjustZoom(map, marker, zoomLevel = 4f)
            }
        }
    }

    private fun TripPointType.toHue(): Float {
        return when (this) {
            TripPointType.ACCOMMODATION -> BitmapDescriptorFactory.HUE_AZURE
            TripPointType.ACTIVITY -> BitmapDescriptorFactory.HUE_ORANGE
            TripPointType.PLACE -> BitmapDescriptorFactory.HUE_RED
            TripPointType.TRANSPORT -> BitmapDescriptorFactory.HUE_VIOLET
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap?.isMyLocationEnabled = true
            return
        }
        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    @SuppressLint("MissingPermission")
    val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.any { it.value }) {
            googleMap?.isMyLocationEnabled = true
            googleMap?.zoomToCurrentLocation(requireContext(), CURRENT_LOCATION_ZOOM)
        }
    }

    companion object {
        private const val CURRENT_LOCATION_ZOOM = 10f
    }
}