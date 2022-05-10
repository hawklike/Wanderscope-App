package cz.cvut.fit.steuejan.wanderscope.app.util

import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

suspend fun getDateFromDatePicker(dateInMillis: Long): String {
    return withDefault {
        DateTime(dateInMillis, DateTimeZone.UTC).toString(DateTimeFormat.mediumDate())
    }
}