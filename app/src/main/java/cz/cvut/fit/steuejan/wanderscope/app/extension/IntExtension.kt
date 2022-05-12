package cz.cvut.fit.steuejan.wanderscope.app.extension

import android.content.res.Resources
import android.util.TypedValue

fun Int.toDp(resources: Resources) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics
).toInt()