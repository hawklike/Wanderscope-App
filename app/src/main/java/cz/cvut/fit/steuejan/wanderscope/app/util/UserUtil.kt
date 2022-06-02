package cz.cvut.fit.steuejan.wanderscope.app.util

import cz.cvut.fit.steuejan.wanderscope.app.extension.capitalize

fun getName(username: String, displayName: String?) = displayName ?: username

fun createAcronym(names: List<String>): String? {
    return when {
        names.isEmpty() -> null
        names.size == 1 -> names.firstOrNull()?.firstOrNull()?.toString()?.capitalize()
        names.size > 1 -> {
            val first = names.firstOrNull()?.firstOrNull()?.toString()?.capitalize()
            val last = names.lastOrNull()?.firstOrNull()?.toString()?.capitalize()
            multipleLet(first, last) { f, l -> f + l }
        }
        else -> null
    }
}