package cz.cvut.fit.steuejan.wanderscope.account.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class DateRequest(
    val YYYY: Int,
    val MM: Int,
    val dd: Int,
    val hh: Int,
    val mm: Int?
)

data class DateResponse(
    val time: String
)

interface AccountApi {

    @POST("/time")
    suspend fun postDate(@Body date: DateRequest): Response<DateResponse>
}
