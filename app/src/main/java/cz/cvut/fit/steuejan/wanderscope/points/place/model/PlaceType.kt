package cz.cvut.fit.steuejan.wanderscope.points.place.model

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.points.common.PointType

enum class PlaceType : PointType {
    PARKING, FOOD, NATURE, OTHER;

    override fun toIcon(): Int {
        return when (this) {
            PARKING -> R.drawable.ic_parking
            FOOD -> R.drawable.ic_food
            NATURE -> R.drawable.ic_nature
            OTHER -> R.drawable.ic_place
        }
    }

    override fun toStringRes(): Int {
        return when (this) {
            PARKING -> R.string.parking
            FOOD -> R.string.food
            NATURE -> R.string.nature
            OTHER -> R.string.other
        }
    }
}