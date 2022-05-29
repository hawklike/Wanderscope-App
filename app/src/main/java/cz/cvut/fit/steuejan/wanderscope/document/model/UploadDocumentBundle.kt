package cz.cvut.fit.steuejan.wanderscope.document.model

import android.os.Parcelable
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadDocumentBundle(
    val tripId: Int,
    val pointId: Int? = null,
    val pointType: TripPointType? = null
) : Parcelable