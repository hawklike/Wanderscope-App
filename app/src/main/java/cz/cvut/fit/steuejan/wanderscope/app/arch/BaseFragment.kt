package cz.cvut.fit.steuejan.wanderscope.app.arch

import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import cz.cvut.fit.steuejan.wanderscope.app.nav.WithBottomNavigationBar
import cz.cvut.fit.steuejan.wanderscope.app.toolbar.WithToolbar
import cz.cvut.fit.steuejan.wanderscope.app.util.runOrLogException
import cz.cvut.fit.steuejan.wanderscope.auth.WithLogin

abstract class BaseFragment : Fragment() {

    open val hasBottomNavigation = true
    open val hasToolbar = true
    open val hasTitle = true
    open val title: String? = null

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
            parentFragment?.findNavController()?.navigate(destinationId, bundle)
                ?: findNavController().navigate(destinationId, bundle)
        }
    }

    protected fun navigateTo(action: NavDirections) {
        runOrLogException {
            parentFragment?.findNavController()?.navigate(action)
                ?: findNavController().navigate(action)
        }
    }

    protected fun navigateBack() {
        runOrLogException {
            parentFragment?.findNavController()?.popBackStack()
                ?: findNavController().popBackStack()
        }
    }

    protected fun login() {
        (activity as? WithLogin)?.login()
    }

    protected fun logout() {
        (activity as? WithLogin)?.logout()
    }

    protected fun setStatusBarColor(@ColorRes color: Int) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            requireActivity().window.apply {
                statusBarColor = ContextCompat.getColor(requireContext(), color)
            }
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

        if (!hasTitle) {
            setTitle(null)
        }

        title?.let(::setTitle)
    }

    private fun setTitle(title: String?) {
        (activity as? WithToolbar)?.setTitle(title)
    }
}