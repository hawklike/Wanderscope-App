package cz.cvut.fit.steuejan.wanderscope.app.extension

import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.DurationItem
import cz.cvut.fit.steuejan.wanderscope.app.util.multipleLet
import org.joda.time.Days
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

fun Duration.getDays(): Int? {
    return multipleLet(startDate, endDate) { s, e ->
        Days.daysBetween(s.toLocalDate(), e.toLocalDate()).days + 1
    }
}

fun Duration.toDurationItem(formatter: DateTimeFormatter = DateTimeFormat.mediumDate()): DurationItem? {
    if (startDate == null && endDate == null) {
        return null
    }
    val startDate = startDate?.toString(formatter) ?: "???"
    val endDate = endDate?.toString(formatter) ?: "???"
    return DurationItem(startDate, endDate)
}