package cz.cvut.fit.steuejan.wanderscope.app.extension

import java.util.*

fun String.capitalize(locale: Locale = Locale.getDefault()): String {
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(locale) else it.toString()
    }
}

fun String?.getOrNullIfBlank() = takeUnless { it.isNullOrBlank() }

fun String.toBase64(): String = Base64.getEncoder().encodeToString(this.toByteArray())