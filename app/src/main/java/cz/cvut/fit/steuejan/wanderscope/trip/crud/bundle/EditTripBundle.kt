package cz.cvut.fit.steuejan.wanderscope.trip.crud.bundle

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.joda.time.DateTime

@Parcelize
data class EditTripBundle(
    val id: Int,
    val name: String,
    val startDate: DateTime?,
    val endDate: DateTime?,
    val description: String?
) : Parcelable