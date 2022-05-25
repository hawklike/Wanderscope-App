package cz.cvut.fit.steuejan.wanderscope.app.binding

import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
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

@BindingAdapter("topMargin")
fun View.setTopMargin(@DimenRes dimen: Int) {
    val marginParams = layoutParams as? ViewGroup.MarginLayoutParams
    marginParams?.let {
        it.topMargin = resources.getDimension(dimen).toInt()
        requestLayout()
    }
}

@BindingAdapter("bottomMargin")
fun View.setBottomMargin(@DimenRes dimen: Int) {
    val marginParams = layoutParams as? ViewGroup.MarginLayoutParams
    marginParams?.let {
        it.bottomMargin = resources.getDimension(dimen).toInt()
        requestLayout()
    }
}

@BindingAdapter("startMargin")
fun View.setStartMargin(@DimenRes dimen: Int) {
    val marginParams = layoutParams as? ViewGroup.MarginLayoutParams
    marginParams?.let {
        it.leftMargin = resources.getDimension(dimen).toInt()
        requestLayout()
    }
}

@BindingAdapter("endMargin")
fun View.setEndMargin(@DimenRes dimen: Int) {
    val marginParams = layoutParams as? ViewGroup.MarginLayoutParams
    marginParams?.let {
        it.rightMargin = resources.getDimension(dimen).toInt()
        requestLayout()
    }
}