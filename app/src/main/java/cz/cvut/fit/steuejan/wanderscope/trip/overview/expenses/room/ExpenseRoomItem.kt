package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.room

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.ChipInfo
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem

data class ExpenseRoomItem(
    val id: Int,
    val name: String,
    val currency: String,
    val persons: List<ChipInfo>,
    val firstItem: Boolean
) : RecyclerItem {

    override val layoutId = R.layout.item_expense_room

    override fun isSameItem(other: RecyclerItem): Boolean {
        return other is ExpenseRoomItem && id == other.id
    }

    override fun hasSameContent(other: RecyclerItem): Boolean {
        return other is ExpenseRoomItem &&
                name == other.name &&
                currency == other.currency &&
                persons == other.persons
    }

}
