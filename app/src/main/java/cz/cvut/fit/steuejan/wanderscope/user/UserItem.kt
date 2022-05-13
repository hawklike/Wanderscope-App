package cz.cvut.fit.steuejan.wanderscope.user

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole

data class UserItem(
    val id: Int,
    val name: String,
    val role: UserRole,
    val hasMenu: Boolean
) : RecyclerItem {

    override val layoutId = R.layout.item_trip_user

    override fun isSameItem(other: RecyclerItem): Boolean {
        return other is UserItem && id == other.id
    }

    override fun hasSameContent(other: RecyclerItem): Boolean {
        return other is UserItem && name == other.name && role == other.role
    }
}
