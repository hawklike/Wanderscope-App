package cz.cvut.fit.steuejan.wanderscope.trip.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Load : Parcelable {
    ALL, TRANSPORT, ACCOMMODATION, PLACES, ACTIVITIES, DOCUMENTS, USERS, TRIP
}