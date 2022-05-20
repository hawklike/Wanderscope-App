package cz.cvut.fit.steuejan.wanderscope.app.arch.viewpager

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseFragment
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.SharedViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import kotlin.reflect.KClass

abstract class ViewPagerFragment<B : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes layoutId: Int,
    viewModelClass: KClass<VM>
) : MvvmFragment<B, VM>(layoutId, viewModelClass) {

    protected val viewPagerSharedViewModel: SharedViewModel? by lazy {
        val parentViewModel = (parentFragment as? MvvmFragment<*, *>)?.viewModel
        parentViewModel as? SharedViewModel
    }

    override val hasBottomNavigation: Boolean
        get() = (parentFragment as? BaseFragment)?.hasBottomNavigation
            ?: super.hasBottomNavigation

    protected fun setTitle(title: String) {
        (parentFragment as? WithViewPager)?.setTitle(title)
    }

    protected fun setViewPagerSharedData(data: Any?, onBackground: Boolean = false) {
        if (onBackground) {
            viewPagerSharedViewModel?.sharedData?.postValue(data)
        } else {
            viewPagerSharedViewModel?.sharedData?.value = data
        }
    }

    protected inline fun <reified T> getViewPagerSharedData(): LiveData<T?>? {
        return viewPagerSharedViewModel?.sharedData?.map {
            if (it is T) it else null
        }
    }
}