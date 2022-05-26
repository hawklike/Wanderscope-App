package cz.cvut.fit.steuejan.wanderscope.app.binding

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.DurationString
import cz.cvut.fit.steuejan.wanderscope.app.extension.capitalize
import cz.cvut.fit.steuejan.wanderscope.app.util.model.DaysHoursMinutes
import cz.cvut.fit.steuejan.wanderscope.app.util.model.FullDuration
import cz.cvut.fit.steuejan.wanderscope.app.util.model.Nights

@BindingAdapter("duration")
fun TextView.setDuration(duration: DurationString?) {
    visibleOrGone(duration) ?: return
    this.text = context.getString(R.string.duration_between_two_dates, duration!!.startDate, duration.endDate)
}

@BindingAdapter("days")
fun TextView.setDays(days: Int?) {
    visibleOrGone(days) ?: return
    this.text = context.resources.getQuantityString(R.plurals.days_between_two_dates, days!!, days)
}

@BindingAdapter("nights")
fun TextView.setNights(nights: Int?) {
    visibleOrGone(nights) ?: return
    this.text = context.resources.getQuantityString(R.plurals.nights_between_two_dates, nights!!, nights)
}

@BindingAdapter("textOrGone")
fun TextView.setTextOrGone(text: String?) {
    visibleOrGone(text) ?: return
    this.text = text
}

@BindingAdapter("textOrInvisible")
fun TextView.setTextOrInvisible(text: String?) {
    visibleOrInvisible(text) ?: return
    this.text = text
}

@BindingAdapter("textResOrGone")
fun TextView.setTextOrGone(@StringRes text: Int?) {
    visibleOrGone(text) ?: return
    this.setText(text!!)
}

@BindingAdapter("textRes")
fun TextView.setTextRes(@StringRes text: Int) {
    kotlin.runCatching {
        this.setText(text)
    }
}

@BindingAdapter("textResOrGoneLowercase")
fun TextView.setTextOrGoneLowercase(@StringRes text: Int?) {
    visibleOrGone(text) ?: return
    val lowercase = context.getString(text!!).lowercase()
    this.text = lowercase
}

@BindingAdapter("capitalize")
fun TextView.capitalize(text: String) {
    this.text = text.capitalize()
}

@BindingAdapter("acronym")
fun TextView.setAcronym(name: String) {
    this.text = name.firstOrNull()?.toString()?.capitalize()
}

@BindingAdapter("userRole")
fun TextView.setUserRole(userRole: UserRole) {
    this.setText(userRole.toStringRes())
    this.setCompoundDrawablesWithIntrinsicBounds(userRole.toIcon(), 0, 0, 0)
    this.compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.margin_8)
}

@BindingAdapter("fullDuration")
fun TextView.setFullDuration(dateInfo: FullDuration?) {
    when (dateInfo) {
        is DaysHoursMinutes -> setDaysHoursAndMinutes(dateInfo)
        is Nights -> setNights(dateInfo.nights)
        null -> visibleOrGone(dateInfo)
    }
}

private fun TextView.setDaysHoursAndMinutes(dateInfo: DaysHoursMinutes) {
    visibleOrGone(dateInfo) ?: return

    val days = dateInfo.days
    val hours = dateInfo.hours
    val minutes = dateInfo.minutes

    this.text = when {
        days != 0 -> context.getString(R.string.duration_days_hours_minutes, days, hours, minutes)
        hours != 0 -> context.getString(R.string.duration_hours_minutes, hours, minutes)
        else -> context.getString(R.string.duration_minutes, minutes)
    }
}