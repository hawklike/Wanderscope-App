package cz.cvut.fit.steuejan.wanderscope.app.extension

import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.DurationString
import cz.cvut.fit.steuejan.wanderscope.app.util.multipleLet
import org.joda.time.Days
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

suspend inline fun Duration.getDays(): Int? {
    return getNights()?.plus(1)
}

suspend inline fun Duration.getNights(): Int? {
    return withDefault {
        multipleLet(startDate, endDate) { s, e ->
            Days.daysBetween(s.toLocalDate(), e.toLocalDate()).days
        }
    }
}

suspend inline fun Duration.toDurationString(
    formatter: DateTimeFormatter = DateTimeFormat.mediumDate()
): DurationString? {
    if (startDate == null && endDate == null) {
        return null
    }
    return withDefault {
        val startDate = startDate?.toString(formatter) ?: "???"
        val endDate = endDate?.toString(formatter) ?: "???"
        DurationString(startDate, endDate)
    }
}