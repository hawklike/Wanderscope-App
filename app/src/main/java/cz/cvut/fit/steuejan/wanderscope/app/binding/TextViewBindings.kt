package cz.cvut.fit.steuejan.wanderscope.app.binding

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.DurationString

@BindingAdapter("duration")
fun TextView.setDuration(duration: DurationString?) {
    visibleOrGone(duration) ?: return
    text = context.getString(R.string.duration_between_two_dates, duration!!.startDate, duration.endDate)
}

@BindingAdapter("days")
fun TextView.setDays(days: Int?) {
    visibleOrGone(days) ?: return
    text = context.resources.getQuantityString(R.plurals.days_between_two_dates, days!!, days)
}

@BindingAdapter("textOrGone")
fun TextView.setTextOrGone(text: String?) {
    visibleOrGone(text) ?: return
    this.text = text
}

@BindingAdapter("textResOrGone")
fun TextView.setTextOrGone(@StringRes text: Int?) {
    visibleOrGone(text) ?: return
    this.setText(text!!)
}