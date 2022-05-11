package cz.cvut.fit.steuejan.wanderscope.app.arch.viewpager

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseFragment
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import kotlin.reflect.KClass

abstract class ViewPagerFragment<B : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes layoutId: Int,
    viewModelClass: KClass<VM>
) : MvvmFragment<B, VM>(layoutId, viewModelClass) {

    override val hasBottomNavigation: Boolean
        get() = (parentFragment as? BaseFragment)?.hasBottomNavigation
            ?: super.hasBottomNavigation

    protected fun setTitle(title: String) {
        (parentFragment as? WithViewPager)?.setTitle(title)
    }
}