package cz.cvut.fit.steuejan.wanderscope.app.util

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault

fun getName(username: String, displayName: String?) = displayName ?: username

suspend fun getColorFromName(name: String): Int {
    var hash = 2
    withDefault {
        name.forEach {
            hash = (hash * 31 + it.code) % 5
        }
    }
    return when (hash) {
        0 -> R.color.colorUser0
        1 -> R.color.colorUser1
        2 -> R.color.colorUser2
        3 -> R.color.colorUser3
        4 -> R.color.colorUser4
        else -> R.color.colorUser3
    }
}