package cz.cvut.fit.steuejan.wanderscope.points.activity.response

import com.squareup.moshi.Json

data class ActivitiesResponse(
    @Json(name = "activities")
    val activities: List<ActivityResponse>
)
