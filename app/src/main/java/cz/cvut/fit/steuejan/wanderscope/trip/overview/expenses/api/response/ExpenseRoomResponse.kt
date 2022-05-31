package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.ChipInfo
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.room.ExpenseRoomItem

data class ExpenseRoomResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "tripId")
    val tripId: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "currency")
    val currency: String,
    @Json(name = "persons")
    val persons: List<String>
) {

    suspend fun toItem(firstItem: Boolean) = ExpenseRoomItem(
        id,
        name,
        currency,
        personsToChipInfo(),
        firstItem
    )

    private suspend fun personsToChipInfo(): List<ChipInfo> {
        return withDefault {
            persons.map {
                ChipInfo(
                    it,
                    isCloseIconVisible = false,
                    textColor = R.color.colorPrimary
                )
            }
        }
    }
}
