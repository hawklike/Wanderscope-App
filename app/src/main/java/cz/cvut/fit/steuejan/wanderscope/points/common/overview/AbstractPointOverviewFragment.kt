package cz.cvut.fit.steuejan.wanderscope.points.common.overview

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import kotlin.reflect.KClass

abstract class AbstractPointOverviewFragment<B : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes layoutId: Int,
    viewModelClass: KClass<VM>
) : MvvmFragment<B, VM>(
    layoutId,
    viewModelClass
), WithLoading, WithRecycler {

    override val hasBottomNavigation = false
    override val hasTitle = false

    abstract val tripId: Int
    abstract val pointId: Int
    abstract val name: String

    private val abstractViewModel by lazy {
        viewModel as? AbstractPointOverviewFragmentVM<*>
    }

    protected abstract fun handleResponse(response: PointResponse)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(name)
        retrievePointOverview()
    }

    protected abstract fun setTitle(title: String)

    private fun retrievePointOverview() {
        showLoading()
        abstractViewModel?.getPoint(tripId, pointId)

        abstractViewModel?.loading?.safeObserve { loading ->
            if (!loading) {
                hideLoading()
            }
        }

        abstractViewModel?.pointOverview?.safeObserve {
            setTitle(it.name)
            handleResponse(it)
        }
    }
}