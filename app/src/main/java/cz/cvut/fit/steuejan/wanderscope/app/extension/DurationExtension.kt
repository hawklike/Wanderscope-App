package cz.cvut.fit.steuejan.wanderscope.app.extension

import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.DurationInItem
import cz.cvut.fit.steuejan.wanderscope.app.util.multipleLet
import org.joda.time.Days
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

suspend fun Duration.getDays(): Int? {
    return withDefault {
        multipleLet(startDate, endDate) { s, e ->
            Days.daysBetween(s.toLocalDate(), e.toLocalDate()).days + 1
        }
    }
}

suspend fun Duration.toDurationItem(formatter: DateTimeFormatter = DateTimeFormat.mediumDate()): DurationInItem? {
    if (startDate == null && endDate == null) {
        return null
    }
    return withDefault {
        val startDate = startDate?.toString(formatter) ?: "???"
        val endDate = endDate?.toString(formatter) ?: "???"
        DurationInItem(startDate, endDate)
    }
}