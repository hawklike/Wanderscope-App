package cz.cvut.fit.steuejan.wanderscope.app.util

import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import cz.cvut.fit.steuejan.wanderscope.points.transport.model.TransportType
import org.joda.time.DateTime

fun saveEventToCalendar(
    startDate: DateTime?,
    endDate: DateTime?,
    allDay: Boolean,
    title: String?,
    description: String?,
    location: String?
) = Intent(Intent.ACTION_INSERT).apply {
    data = CalendarContract.Events.CONTENT_URI
    putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, allDay)
    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDate?.millis)
    putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDate?.millis)
    putExtra(CalendarContract.Events.TITLE, title)
    putExtra(CalendarContract.Events.DESCRIPTION, description)
    putExtra(CalendarContract.Events.EVENT_LOCATION, location)
}

fun goToWebsite(url: String): Intent {
    var safeUrl = url
    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        safeUrl = "http://$url"
    }
    return Intent(Intent.ACTION_VIEW, Uri.parse(safeUrl))
}

fun showMap(address: String) = Intent(Intent.ACTION_VIEW).apply {
    data = Uri.parse("geo:0,0?q=$address")
}

fun showMap(latitude: String, longitude: String) = Intent(Intent.ACTION_VIEW).apply {
    data = Uri.parse("geo:$latitude,$longitude")
}

fun callPhone(phoneNumber: String) = Intent(Intent.ACTION_DIAL).apply {
    data = Uri.parse("tel:$phoneNumber")
}

fun sendEmail(vararg address: String, subject: String? = null) = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_EMAIL, address)
    putExtra(Intent.EXTRA_SUBJECT, subject)
}

fun showDirections(from: String?, to: String, transport: TransportType?): Intent {
    val base = "https://www.google.com/maps/dir/?api=1"
    val uri = Uri.parse(base)
        .buildUpon()
        .appendQueryParameter("origin", from)
        .appendQueryParameter("destination", to)
        .appendQueryParameter("travelmode", transport?.toTravelMode())
        .build()
    return Intent(Intent.ACTION_VIEW, uri)
}

private fun TransportType.toTravelMode(): String {
    return when (this) {
        TransportType.WALK -> "walking"
        TransportType.BIKE -> "bicycling"
        TransportType.CAR -> "driving"
        else -> "transit"
    }
}