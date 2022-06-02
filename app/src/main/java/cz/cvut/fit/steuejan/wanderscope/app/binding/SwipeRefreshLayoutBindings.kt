package cz.cvut.fit.steuejan.wanderscope.app.binding

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("progressColor")
fun SwipeRefreshLayout.setProgressColor(color: Int) {
    this.setColorSchemeColors(color)
}