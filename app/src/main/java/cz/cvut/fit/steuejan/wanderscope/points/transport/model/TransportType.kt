package cz.cvut.fit.steuejan.wanderscope.points.transport.model

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.points.common.PointType

enum class TransportType : PointType {
    WALK, BIKE, CAR, BUS, TRAIN, FERRY, PUBLIC, PLANE, OTHER;

    override val position: Int get() = this.ordinal

    override fun toIcon(): Int {
        return when (this) {
            WALK -> R.drawable.ic_walking
            BIKE -> R.drawable.ic_bike
            CAR -> R.drawable.ic_car
            BUS -> R.drawable.ic_bus
            TRAIN -> R.drawable.ic_train
            FERRY -> R.drawable.ic_boat
            PUBLIC -> R.drawable.ic_public_transport
            PLANE -> R.drawable.ic_light_aircraft
            OTHER -> R.drawable.ic_route
        }
    }

    override fun toStringRes(): Int {
        return when (this) {
            WALK -> R.string.walk
            BIKE -> R.string.bike
            CAR -> R.string.car
            BUS -> R.string.bus
            TRAIN -> R.string.train
            FERRY -> R.string.ferry
            PUBLIC -> R.string.public_transport
            PLANE -> R.string.plane
            OTHER -> R.string.other
        }
    }
}