package cz.cvut.fit.steuejan.wanderscope.points.place.model

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.points.common.PointType

enum class PlaceType : PointType {
    PARKING, RESTAURANT, COUNTRYSIDE, CITY, MUSEUM, ZOO, PARK, MONUMENT, MOUNTAINS, CASTLE, OTHER;

    override val position: Int get() = this.ordinal

    override fun toIcon(): Int {
        return when (this) {
            PARKING -> R.drawable.ic_parking
            RESTAURANT -> R.drawable.ic_food
            COUNTRYSIDE -> R.drawable.ic_countryside
            CITY -> R.drawable.ic_city
            MUSEUM -> R.drawable.ic_museum
            ZOO -> R.drawable.ic_zoo
            PARK -> R.drawable.ic_nature
            MONUMENT -> R.drawable.ic_eiffel_tower
            CASTLE -> R.drawable.ic_castle
            MOUNTAINS -> R.drawable.ic_mountain
            OTHER -> R.drawable.ic_place
        }
    }

    override fun toStringRes(): Int {
        return when (this) {
            PARKING -> R.string.parking
            RESTAURANT -> R.string.restaurant
            COUNTRYSIDE -> R.string.countryside
            CITY -> R.string.city
            MUSEUM -> R.string.museum
            ZOO -> R.string.zoo
            PARK -> R.string.park
            MONUMENT -> R.string.monument
            MOUNTAINS -> R.string.mountains
            CASTLE -> R.string.castle
            OTHER -> R.string.other
        }
    }

    override fun toStringOverviewRes(): Int {
        return when (this) {
            PARKING -> R.string.parking_overview
            RESTAURANT -> R.string.restaurant_overview
            COUNTRYSIDE -> R.string.countryside_overview
            CITY -> R.string.city_overview
            MUSEUM -> R.string.museum_overview
            ZOO -> R.string.zoo_overview
            PARK -> R.string.park_overview
            MONUMENT -> R.string.monument_overview
            MOUNTAINS -> R.string.mountains_overview
            CASTLE -> R.string.castle_overview
            OTHER -> R.string.place_overview_other
        }
    }
}