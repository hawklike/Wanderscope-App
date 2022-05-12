package cz.cvut.fit.steuejan.wanderscope.points.transport.response

import com.squareup.moshi.Json

data class TransportsResponse(
    @Json(name = "transports")
    val transports: List<TransportResponse>
)
