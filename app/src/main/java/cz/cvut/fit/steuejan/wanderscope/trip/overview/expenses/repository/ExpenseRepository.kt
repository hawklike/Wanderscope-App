package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.CreatedResponse
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.ExpenseApi
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.request.ExpenseRoomRequest
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.response.ExpenseRoomsResponse
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseApi: ExpenseApi) {

    suspend fun getExpenseRooms(tripId: Int): Flow<Result<ExpenseRoomsResponse>> {
        return performCall { expenseApi.getExpenseRooms(tripId) }
    }

    suspend fun createExpenseRoom(tripId: Int, request: ExpenseRoomRequest): Flow<Result<CreatedResponse>> {
        return performCall { expenseApi.createExpenseRoom(tripId, request) }
    }
}