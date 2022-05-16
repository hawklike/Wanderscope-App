package cz.cvut.fit.steuejan.wanderscope.points.activity.api.response

import com.squareup.moshi.Json

data class ActivitiesResponse(
    @Json(name = "activities")
    val activities: List<ActivityResponse>
)
