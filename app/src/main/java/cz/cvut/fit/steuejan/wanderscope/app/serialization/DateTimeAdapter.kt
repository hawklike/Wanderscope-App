package cz.cvut.fit.steuejan.wanderscope.app.serialization

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.joda.time.DateTime

class DateTimeAdapter {
    @ToJson
    fun toJson(date: DateTime): String = date.toString()

    @FromJson
    fun fromJson(date: String): DateTime = DateTime.parse(date)
}