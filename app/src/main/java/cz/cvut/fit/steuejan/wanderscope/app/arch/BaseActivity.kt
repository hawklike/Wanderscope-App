package cz.cvut.fit.steuejan.wanderscope.app.arch

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import cz.cvut.fit.steuejan.wanderscope.app.util.runOrLogException

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var navController: NavController

    protected inline fun <T> LiveData<T>.safeObserve(crossinline callback: (T) -> Unit) {
        this.observe(this@BaseActivity) {
            callback.invoke(it)
        }
    }

    protected fun navigateTo(@IdRes destinationId: Int, bundle: Bundle? = null) {
        runOrLogException {
            navController.navigate(destinationId, bundle)
        }
    }

    protected fun navigateTo(action: NavDirections) {
        runOrLogException {
            navController.navigate(action)
        }
    }
}