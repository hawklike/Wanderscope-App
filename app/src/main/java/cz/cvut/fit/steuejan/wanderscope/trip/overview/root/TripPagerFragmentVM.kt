package cz.cvut.fit.steuejan.wanderscope.trip.overview.root

import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.SharedViewModel

class TripPagerFragmentVM : BaseViewModel(), SharedViewModel {
    override val sharedData = MutableLiveData<Any?>()
}