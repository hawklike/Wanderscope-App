package cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item

import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem

data class EmptyItem(
    val title: String,
    val subtitle: String?
) : RecyclerItem {

    override val layoutId: Int
        get() = TODO("Not yet implemented")

    override fun isSameItem(other: RecyclerItem): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasSameContent(other: RecyclerItem): Boolean {
        TODO("Not yet implemented")
    }
}
