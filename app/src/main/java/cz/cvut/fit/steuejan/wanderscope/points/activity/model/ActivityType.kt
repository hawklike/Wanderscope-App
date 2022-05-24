package cz.cvut.fit.steuejan.wanderscope.points.activity.model

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.points.common.PointType

enum class ActivityType : PointType {
    HIKING, CYCLING, SKIING, RUNNING, KAYAK, SWIMMING, CLIMBING, CROSS_COUNTRY, OTHER;

    override val position: Int get() = this.ordinal

    override fun toIcon(): Int {
        return when (this) {
            HIKING -> R.drawable.ic_hiking
            CYCLING -> R.drawable.ic_bike
            SKIING -> R.drawable.ic_skiing
            RUNNING -> R.drawable.ic_running
            KAYAK -> R.drawable.ic_kayak
            SWIMMING -> R.drawable.ic_swimming
            CLIMBING -> R.drawable.ic_climbing
            CROSS_COUNTRY -> R.drawable.ic_skiing
            OTHER -> R.drawable.ic_trophy
        }
    }

    override fun toStringRes(): Int {
        return when (this) {
            HIKING -> R.string.hiking
            CYCLING -> R.string.cycling
            SKIING -> R.string.skiing
            RUNNING -> R.string.running
            KAYAK -> R.string.kayak
            SWIMMING -> R.string.swimming
            CLIMBING -> R.string.climbing
            CROSS_COUNTRY -> R.string.cross_country
            OTHER -> R.string.other
        }
    }
}