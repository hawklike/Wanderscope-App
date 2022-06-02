package cz.cvut.fit.steuejan.wanderscope.app.util.model

sealed class FullDuration

data class Nights(
    val nights: Int
) : FullDuration()

data class DaysHoursMinutes(
    val days: Int,
    val hours: Int,
    val minutes: Int
) : FullDuration()
