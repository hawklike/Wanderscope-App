package cz.cvut.fit.steuejan.wanderscope.app.util

import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

suspend fun getDateFromMillis(dateInMillis: Long): String {
    return withDefault {
        DateTime(dateInMillis, DateTimeZone.UTC).toString(DateTimeFormat.mediumDate())
    }
}

fun getDaysHoursAndMinutes(duration: Duration): DaysHoursMinutes? {
    return getDaysHoursAndMinutes(duration.startDate, duration.endDate)
}

fun getDaysHoursAndMinutes(startDate: DateTime?, endDate: DateTime?): DaysHoursMinutes? {
    return multipleLet(startDate, endDate) { start, end ->
        val duration = org.joda.time.Duration(start, end)
        DaysHoursMinutes(
            days = duration.standardDays.toInt(),
            hours = duration.standardHours.toInt() % 24,
            minutes = duration.standardMinutes.toInt() % 60
        )
    }
}

data class DaysHoursMinutes(
    val days: Int,
    val hours: Int,
    val minutes: Int
)