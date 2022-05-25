package cz.cvut.fit.steuejan.wanderscope.app.binding

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleOrGone")
fun View.visibleOrGone(any: Any?): Any? {
    visibility = if (any != null) View.VISIBLE else View.GONE
    return any
}

@BindingAdapter("visibleOrGone")
fun View.visibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleOrInvisible")
fun View.visibleOrInvisible(any: Any?): Any? {
    visibility = if (any != null) View.VISIBLE else View.INVISIBLE
    return any
}

@BindingAdapter("backgroundTintResource")
fun View.setBackgroundTint(@ColorRes tintColor: Int) {
    background.setTint(ContextCompat.getColor(context, tintColor))
}