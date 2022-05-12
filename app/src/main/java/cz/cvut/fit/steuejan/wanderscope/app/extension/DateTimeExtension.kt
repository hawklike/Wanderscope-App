package cz.cvut.fit.steuejan.wanderscope.app.extension

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

suspend inline infix fun Long.isBefore(other: Long) = withDefault {
    DateTime(this@isBefore, DateTimeZone.UTC).isBefore(DateTime(other, DateTimeZone.UTC))
}

suspend fun Long.toDateTime() = withDefault {
    DateTime(this@toDateTime, DateTimeZone.UTC)
}

suspend fun DateTime.toNiceString(
    dateFormatter: DateTimeFormatter = DateTimeFormat.mediumDate(),
    timeFormatter: DateTimeFormatter = DateTimeFormat.shortTime(),
    delimiter: String = " "
) = withDefault {
    val date = this@toNiceString.toString(dateFormatter)
    val time = this@toNiceString.toString(timeFormatter)
    "$date$delimiter$time"
}