package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.response

import com.squareup.moshi.Json

data class ExpensesResponse(
    @Json(name = "expenses")
    val expenses: List<ExpenseResponse>
)
