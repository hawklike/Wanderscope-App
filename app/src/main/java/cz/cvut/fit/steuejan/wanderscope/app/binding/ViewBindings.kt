package cz.cvut.fit.steuejan.wanderscope.app.binding

import android.view.View

fun View.visibleOrGone(any: Any?): Any? {
    visibility = if (any != null) View.VISIBLE else View.GONE
    return any
}