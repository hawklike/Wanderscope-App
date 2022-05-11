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

@BindingAdapter("backgroundTintResource")
fun View.setBackgroundTint(@ColorRes tintColor: Int) {
    backgroundTintList = ContextCompat.getColorStateList(context, tintColor)
}