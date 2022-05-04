package cz.cvut.fit.steuejan.wanderscope.app.binding

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.button.MaterialButton
import cz.cvut.fit.steuejan.wanderscope.R

//https://stackoverflow.com/a/68347420/9723204
@BindingAdapter(value = ["showProgress", "textAfter"], requireAll = true)
fun MaterialButton.setShowProgress(showProgress: Boolean?, textAfter: String) {
    if (showProgress == true) {
        icon = CircularProgressDrawable(context).apply {
            setStyle(CircularProgressDrawable.DEFAULT)
            setColorSchemeColors(ContextCompat.getColor(context, R.color.purple_500))
            start()
        }
        icon.callback = object : Drawable.Callback {
            override fun invalidateDrawable(p0: Drawable) {
                this@setShowProgress.invalidate()
            }

            override fun scheduleDrawable(p0: Drawable, p1: Runnable, p2: Long) {}
            override fun unscheduleDrawable(p0: Drawable, p1: Runnable) {}
        }
        text = null
        iconPadding = 0
        iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
        isEnabled = false
    } else if (showProgress == false) {
        icon = null
        isEnabled = true
        text = textAfter
    }
}