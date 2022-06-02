package cz.cvut.fit.steuejan.wanderscope.points.common

enum class TripPointType {
    ACCOMMODATION, ACTIVITY, PLACE, TRANSPORT;

    override fun toString(): String {
        return when (this) {
            ACCOMMODATION -> "accommodation"
            ACTIVITY -> "activity"
            PLACE -> "place"
            TRANSPORT -> "transport"
        }
    }
}