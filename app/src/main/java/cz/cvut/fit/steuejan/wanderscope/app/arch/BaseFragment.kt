package cz.cvut.fit.steuejan.wanderscope.app.arch

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import cz.cvut.fit.steuejan.wanderscope.MainActivityVM
import cz.cvut.fit.steuejan.wanderscope.app.nav.WithBottomNavigationBar
import cz.cvut.fit.steuejan.wanderscope.app.toolbar.WithToolbar
import cz.cvut.fit.steuejan.wanderscope.app.util.runOrLogException
import cz.cvut.fit.steuejan.wanderscope.auth.WithLogin
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseFragment : Fragment() {

    open val hasBottomNavigation = true
    open val hasToolbar = true
    open val hasTitle = true
    open val title: String? = null

    protected open val sharedViewModel: SharedViewModel by sharedViewModel<MainActivityVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBottomNavigation()
        handleToolbar()
    }

    protected fun setSharedData(data: Any?, onBackground: Boolean = false) {
        if (onBackground) {
            sharedViewModel.sharedData.postValue(data)
        } else {
            sharedViewModel.sharedData.value = data
        }
    }

    protected inline fun <reified T> getSharedData(): LiveData<T?> {
        return sharedViewModel.sharedData.map {
            if (it is T) it else null
        }
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

    protected open fun hideKeyboard() {
        requireActivity().currentFocus?.let { view ->
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    protected fun showToast(toast: BaseViewModel.ToastInfo) {
        Toast.makeText(requireContext(), toast.message, toast.lenght).show()
    }

    protected fun showToast(@StringRes message: Int) {
        showToast(BaseViewModel.ToastInfo(message))
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