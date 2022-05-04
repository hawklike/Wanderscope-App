package cz.cvut.fit.steuejan.wanderscope.app.binding

import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorText")
fun TextInputLayout.setError(@StringRes message: Int?) {
    val text = message?.let { context.getString(it) }
    this.error = text
}