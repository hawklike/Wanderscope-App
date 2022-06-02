package cz.cvut.fit.steuejan.wanderscope.app.util

import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.app.util.model.DaysHoursMinutes
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

suspend fun getDateFromMillis(dateInMillis: Long): String {
    return withDefault {
        DateTime(dateInMillis, DateTimeZone.UTC).toString(DateTimeFormat.mediumDate())
    }
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