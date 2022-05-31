package cz.cvut.fit.steuejan.wanderscope.app.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator

@BindingAdapter("showLoading")
fun CircularProgressIndicator.showLoading(loading: Boolean) {
    if (loading) this.show() else this.hide()
}