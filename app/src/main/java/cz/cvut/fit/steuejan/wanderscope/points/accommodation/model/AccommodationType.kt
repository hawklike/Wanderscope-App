package cz.cvut.fit.steuejan.wanderscope.points.accommodation.model

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.points.common.PointType

enum class AccommodationType : PointType {
    HOTEL, HOSTEL, PENSION, CAMP, OUTDOOR, AIRBNB;

    override fun toIcon(): Int {
        return when (this) {
            HOTEL -> R.drawable.ic_hotel
            HOSTEL -> R.drawable.ic_hostel
            PENSION -> R.drawable.ic_pension
            CAMP -> R.drawable.ic_camp
            OUTDOOR -> R.drawable.ic_sleeping_bag
            AIRBNB -> R.drawable.ic_airbnb
        }
    }
}