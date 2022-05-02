package cz.cvut.fit.steuejan.wanderscope.app.arch

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData

abstract class BaseActivity : AppCompatActivity() {

    protected inline fun <T> LiveData<T>.observe(crossinline callback: (T) -> Unit) {
        this.observe(this@BaseActivity) {
            callback.invoke(it)
        }
    }
}