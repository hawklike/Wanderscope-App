package cz.cvut.fit.steuejan.wanderscope.app.binding

import android.view.View
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator
import timber.log.Timber

@BindingAdapter("errorText")
fun TextInputLayout.setError(@StringRes message: Int?) {
    this.error = if (message != null && message != InputValidator.OK) {
        context.getString(message)
    } else {
        null
    }
}

@BindingAdapter("cancellable")
fun TextInputLayout.cancellable(allowed: Boolean) {
    if (allowed) {
        this.setEndIconOnClickListener {
            this.editText?.text = null
            this.error = null
        }
    }
}

@BindingAdapter("onEndDrawableClick")
fun TextInputLayout.onEndDrawableClick(listener: View.OnClickListener) {
    this.setEndIconOnClickListener(listener)
}

@BindingAdapter("endDrawableEnabled")
fun TextInputLayout.isEndDrawableEnabled(enable: Boolean?) {
    this.isEndIconVisible = enable == true
}

@BindingAdapter("endDrawableActive")
fun TextInputLayout.isEndDrawableActive(enable: Boolean) {
    Timber.d("here: $enable")
    val activeColor = context.getColor(R.color.colorPrimary)
    val inactiveColor = context.getColor(R.color.colorTextInputIcon)
    val color = if (enable) activeColor else inactiveColor
    this.endIconDrawable?.setTint(color)
}