package cz.cvut.fit.steuejan.wanderscope.app.extension

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

suspend inline infix fun Long.isBefore(other: Long) = withDefault {
    DateTime(this@isBefore, DateTimeZone.UTC).isBefore(DateTime(other, DateTimeZone.UTC))
}