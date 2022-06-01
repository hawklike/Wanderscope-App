package cz.cvut.fit.steuejan.wanderscope.points.accommodation.model

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.points.common.PointType

enum class AccommodationType : PointType {
    HOTEL, HOSTEL, PENSION, CAMP, OUTDOOR, AIRBNB, OTHER;

    override val position: Int get() = this.ordinal

    override fun toIcon(): Int {
        return when (this) {
            HOTEL -> R.drawable.ic_hotel
            HOSTEL -> R.drawable.ic_hostel
            PENSION -> R.drawable.ic_pension
            CAMP -> R.drawable.ic_camp
            OUTDOOR -> R.drawable.ic_sleeping_bag
            AIRBNB -> R.drawable.ic_airbnb
            OTHER -> R.drawable.ic_accommodation_other
        }
    }

    override fun toStringRes(): Int {
        return when (this) {
            HOTEL -> R.string.hotel
            HOSTEL -> R.string.hostel
            PENSION -> R.string.pension
            CAMP -> R.string.camp
            OUTDOOR -> R.string.outdoor
            AIRBNB -> R.string.airbnb
            OTHER -> R.string.other
        }
    }

    override fun toStringOverviewRes(): Int {
        return when (this) {
            HOTEL -> R.string.hotel_overview
            HOSTEL -> R.string.hostel_overview
            PENSION -> R.string.pension_overview
            CAMP -> R.string.camp_overview
            OUTDOOR -> R.string.outdoor_overview
            AIRBNB -> R.string.airbnb_overview
            OTHER -> R.string.accommodation_other_overview
        }
    }
}