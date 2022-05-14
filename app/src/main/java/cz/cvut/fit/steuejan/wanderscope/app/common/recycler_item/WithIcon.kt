package cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item

import androidx.annotation.DrawableRes

interface WithIcon {
    @DrawableRes
    fun toIcon(): Int
}