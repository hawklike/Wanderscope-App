package cz.cvut.fit.steuejan.wanderscope

import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent.Destination

class FirstFragmentVM : BaseViewModel() {
    fun doJob() {
        navigateTo(Destination(R.id.SecondFragment))
    }
}