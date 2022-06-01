package cz.cvut.fit.steuejan.wanderscope.points.common

import androidx.annotation.StringRes
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.WithIcon

interface PointType : WithIcon {
    @StringRes
    fun toStringRes(): Int

    @StringRes
    fun toStringOverviewRes(): Int

    val position: Int
}