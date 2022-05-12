package cz.cvut.fit.steuejan.wanderscope.points.common

import androidx.annotation.DrawableRes

interface PointType {
    @DrawableRes
    fun toIcon(): Int
}