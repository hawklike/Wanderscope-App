package cz.cvut.fit.steuejan.wanderscope.app.extension

import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.DurationString
import cz.cvut.fit.steuejan.wanderscope.app.util.getDaysHoursAndMinutes
import cz.cvut.fit.steuejan.wanderscope.app.util.model.DaysHoursMinutes
import cz.cvut.fit.steuejan.wanderscope.app.util.model.Nights
import cz.cvut.fit.steuejan.wanderscope.app.util.multipleLet
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

suspend inline fun Duration.getDays(): Int? {
    return getNights()?.nights?.plus(1)
}

suspend inline fun Duration.getNights(): Nights? {
    return withDefault {
        multipleLet(startDate, endDate) { s, e ->
            Nights(Days.daysBetween(s.toLocalDate(), e.toLocalDate()).days)
        }
    }
}

fun Duration.getDaysHoursAndMinutes(): DaysHoursMinutes? {
    return getDaysHoursAndMinutes(startDate, endDate)
}

suspend fun Duration.isNow(): Boolean {
    return multipleLet(startDate, endDate) { startDate, endDate ->
        withDefault {
            val now = DateTime.now().toLocalDateTime()
            startDate.toLocalDateTime() <= now && now <= endDate.toLocalDateTime()
        }
    } ?: false
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

suspend inline fun Duration.getStartTime(
    formatter: DateTimeFormatter = DateTimeFormat.shortTime()
): String? {
    return withDefault {
        startDate?.toString(formatter)
    }
}