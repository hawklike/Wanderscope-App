package cz.cvut.fit.steuejan.wanderscope.app.binding

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.button.MaterialButton
import cz.cvut.fit.steuejan.wanderscope.R

//https://stackoverflow.com/a/68347420/9723204
@BindingAdapter(value = ["showProgress", "textAfterProgress"], requireAll = true)
fun MaterialButton.setShowProgress(showProgress: Boolean?, textAfterProgress: String) {
    if (showProgress == true) {
        icon = CircularProgressDrawable(context).apply {
            setStyle(CircularProgressDrawable.DEFAULT)
            setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary))
            start()
        }
        icon.callback = object : Drawable.Callback {
            override fun invalidateDrawable(p0: Drawable) {
                this@setShowProgress.invalidate()
            }

            override fun scheduleDrawable(p0: Drawable, p1: Runnable, p2: Long) {}
            override fun unscheduleDrawable(p0: Drawable, p1: Runnable) {}
        }
        inProgress()
    } else if (showProgress == false) {
        afterProgress(textAfterProgress)
    }
}

private fun MaterialButton.inProgress() {
    text = null
    iconPadding = 0
    iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
    isClickable = false
    setBackgroundColor(ContextCompat.getColor(context, R.color.colorSecondary))
}

private fun MaterialButton.afterProgress(textAfterProgress: String) {
    icon = null
    isClickable = true
    alpha = 1f
    setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
    text = textAfterProgress
}

@BindingAdapter("enabled")
fun MaterialButton.enable(enable: Boolean?) {
    if (enable == true) {
        isClickable = true
        alpha = 1f
    } else {
        isClickable = false
        alpha = 0.5f
    }
}