package cz.cvut.fit.steuejan.wanderscope.trips.repository

import androidx.core.text.htmlEncode
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.trips.api.TripsApi
import cz.cvut.fit.steuejan.wanderscope.trips.api.response.TripsResponse
import cz.cvut.fit.steuejan.wanderscope.trips.model.TripsScope
import kotlinx.coroutines.flow.Flow
import org.joda.time.DateTime

class TripsRepository(private val tripsApi: TripsApi) {

    suspend fun getTrips(
        tripScope: TripsScope,
        date: DateTime = DateTime.now()
    ): Flow<Result<TripsResponse>> {
        val iso8601Date = date.toString()
        return performCall { tripsApi.getTrips(tripScope, iso8601Date.htmlEncode()) }
    }

}