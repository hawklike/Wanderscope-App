package cz.cvut.fit.steuejan.wanderscope.app.nav

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavDirections

sealed class NavigationEvent {
    class Action(val action: NavDirections) : NavigationEvent()
    class Destination(@IdRes val destinationId: Int, val bundle: Bundle? = null) : NavigationEvent()
}

