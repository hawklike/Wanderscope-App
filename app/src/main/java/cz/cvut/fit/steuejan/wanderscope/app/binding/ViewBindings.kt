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
    setTopMargin(resources.getDimension(dimen))
}

@BindingAdapter("bottomMargin")
fun View.setBottomMargin(@DimenRes dimen: Int) {
    setBottomMargin(resources.getDimension(dimen))
}

@BindingAdapter("startMargin")
fun View.setStartMargin(@DimenRes dimen: Int) {
    setStartMargin(resources.getDimension(dimen))
}

@BindingAdapter("endMargin")
fun View.setEndMargin(@DimenRes dimen: Int) {
    setEndMargin(resources.getDimension(dimen))
}

@BindingAdapter("topMargin")
fun View.setTopMargin(dimen: Float) {
    val marginParams = layoutParams as? ViewGroup.MarginLayoutParams
    marginParams?.let {
        it.topMargin = dimen.toInt()
        requestLayout()
    }
}

@BindingAdapter("bottomMargin")
fun View.setBottomMargin(dimen: Float) {
    val marginParams = layoutParams as? ViewGroup.MarginLayoutParams
    marginParams?.let {
        it.bottomMargin = dimen.toInt()
        requestLayout()
    }
}

@BindingAdapter("startMargin")
fun View.setStartMargin(dimen: Float) {
    val marginParams = layoutParams as? ViewGroup.MarginLayoutParams
    marginParams?.let {
        it.leftMargin = dimen.toInt()
        requestLayout()
    }
}

@BindingAdapter("endMargin")
fun View.setEndMargin(dimen: Float) {
    val marginParams = layoutParams as? ViewGroup.MarginLayoutParams
    marginParams?.let {
        it.rightMargin = dimen.toInt()
        requestLayout()
    }
}