package cz.cvut.fit.steuejan.wanderscope.app.binding

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleOrGone")
fun View.visibleOrGone(any: Any?): Any? {
    visibility = if (any != null) View.VISIBLE else View.GONE
    return any
}