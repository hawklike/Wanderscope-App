package cz.cvut.fit.steuejan.wanderscope.trip.model

import android.os.Parcelable
import cz.cvut.fit.steuejan.wanderscope.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Load : Parcelable {
    ALL, TRANSPORT, ACCOMMODATION, PLACES, ACTIVITIES, DOCUMENTS, USERS, TRIP;

    fun getUpdatingString(): Int? {
        return when (this) {
            ALL -> null
            TRANSPORT -> R.string.updating_transport
            ACCOMMODATION -> R.string.updating_accommodation
            PLACES -> R.string.updating_places
            ACTIVITIES -> R.string.updating_activities
            DOCUMENTS -> R.string.updating_documents
            USERS -> R.string.updating_users
            TRIP -> R.string.updating_trip
        }
    }
}