package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api

import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.request.ExpenseRoomRequest
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.response.ExpenseRoomsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExpenseApi {

    @GET("/trip/{id}/rooms")
    suspend fun getExpenseRooms(@Path("id") id: Int): Response<ExpenseRoomsResponse>

    @POST("/trip/{id}/room")
    suspend fun createExpenseRoom(
        @Path("id") id: Int,
        @Body request: ExpenseRoomRequest
    ): Response<CreatedResponse>
}