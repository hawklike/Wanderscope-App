package cz.cvut.fit.steuejan.wanderscope.app.binding

import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator

@BindingAdapter("errorText")
fun TextInputLayout.setError(@StringRes message: Int?) {
    this.error = if (message != null && message != InputValidator.OK) {
        context.getString(message)
    } else {
        null
    }
}