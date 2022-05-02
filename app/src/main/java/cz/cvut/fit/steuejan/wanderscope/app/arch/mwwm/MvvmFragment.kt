package cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import cz.cvut.fit.steuejan.wanderscope.BR
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseFragment
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class MvvmFragment<B : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes val layoutId: Int,
    viewModelClass: KClass<VM>
) : BaseFragment() {

    protected lateinit var binding: B private set
    protected open val viewModel: VM by lazy { getViewModel(clazz = viewModelClass) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate<B>(inflater, layoutId, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.vm, viewModel)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToNavigate()
        listenToToast()
    }

    override fun onDestroy() {
        lifecycle.removeObserver(viewModel)
        super.onDestroy()
    }

    private fun listenToNavigate() {
        viewModel.navigateEvent.safeObserve {
            when (it) {
                is NavigationEvent.Action -> navigateTo(it.action)
                is NavigationEvent.Destination -> navigateTo(it.destinationId, it.bundle)
            }
        }
    }

    private fun listenToToast() {
        viewModel.toastEvent.safeObserve { toast ->
            Toast.makeText(requireContext(), toast.message, toast.lenght).show()
        }
    }
}