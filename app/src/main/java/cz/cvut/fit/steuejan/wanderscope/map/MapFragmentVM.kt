package cz.cvut.fit.steuejan.wanderscope.map

import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.common.map.LatLngBundle
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.DurationString

class MapFragmentVM : BaseViewModel() {

    val title = MutableLiveData<String>()
    val duration = MutableLiveData<DurationString?>()

    val coordinates = MutableLiveData<List<LatLngBundle>>()

    fun init(title: String, duration: DurationString?) {
        this.title.value = title
        this.duration.value = duration
    }

    fun saveCoordinates(coordinates: List<LatLngBundle>) {
        this.coordinates.value = coordinates
    }
}
