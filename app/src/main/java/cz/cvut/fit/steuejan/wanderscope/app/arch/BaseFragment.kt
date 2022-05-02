package cz.cvut.fit.steuejan.wanderscope.app.arch

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import cz.cvut.fit.steuejan.wanderscope.app.nav.WithBottomNavigationBar
import cz.cvut.fit.steuejan.wanderscope.app.toolbar.WithToolbar
import cz.cvut.fit.steuejan.wanderscope.app.util.runOrLogException

abstract class BaseFragment : Fragment() {

    open val hasBottomNavigation = true
    open val hasToolbar = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBottomNavigation()
        handleToolbar()
    }

    protected inline fun <T> LiveData<T>.safeObserve(crossinline callback: (T) -> Unit) {
        this.observe(viewLifecycleOwner) {
            callback.invoke(it)
        }
    }

    protected fun navigateTo(@IdRes destinationId: Int, bundle: Bundle? = null) {
        runOrLogException {
            findNavController().navigate(destinationId, bundle)
        }
    }

    protected fun navigateTo(action: NavDirections) {
        runOrLogException {
            findNavController().navigate(action)
        }
    }

    private fun handleBottomNavigation() {
        if (hasBottomNavigation) {
            (activity as? WithBottomNavigationBar)?.showBottomNavigation()
        } else {
            (activity as? WithBottomNavigationBar)?.hideBottomNavigation()
        }
    }

    private fun handleToolbar() {
        if (hasToolbar) {
            (activity as? WithToolbar)?.showToolbar()
        } else {
            (activity as? WithToolbar)?.hideToolbar()
        }
    }
}