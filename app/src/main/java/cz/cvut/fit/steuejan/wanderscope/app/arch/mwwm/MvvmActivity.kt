package cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import cz.cvut.fit.steuejan.wanderscope.BR
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseActivity
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class MvvmActivity<B : ViewDataBinding, out VM : BaseViewModel>(
    @LayoutRes var layoutId: Int,
    viewModelClass: KClass<VM>
) : BaseActivity() {

    protected lateinit var binding: B private set
    protected open val viewModel: VM by lazy { getViewModel(clazz = viewModelClass) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)

        binding = DataBindingUtil.setContentView<B>(this, layoutId).apply {
            lifecycleOwner = this@MvvmActivity
            setVariable(BR.vm, viewModel)
        }
    }

    override fun onDestroy() {
        lifecycle.removeObserver(viewModel)
        super.onDestroy()
    }
}