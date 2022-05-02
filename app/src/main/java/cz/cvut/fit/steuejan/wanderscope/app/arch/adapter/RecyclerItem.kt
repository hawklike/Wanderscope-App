package cz.cvut.fit.steuejan.wanderscope.app.arch.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil

interface RecyclerItem {
    @get:LayoutRes
    val layoutId: Int

    fun isSameItem(other: RecyclerItem): Boolean
    fun hasSameContent(other: RecyclerItem): Boolean

    class RecyclerItemlDiffUtil<T : RecyclerItem> : DiffUtil.ItemCallback<T>() {

        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.isSameItem(newItem)
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.hasSameContent(newItem)
        }
    }
}
