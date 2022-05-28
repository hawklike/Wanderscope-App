package cz.cvut.fit.steuejan.wanderscope.app.arch.adapter

import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

interface WithRecycler {

    fun <T : RecyclerItem> setCustomAdapter(
        recyclerView: RecyclerView,
        adapter: DataBindingAdapter<T>,
        onClickListener: ((item: T, position: Int) -> Unit)? = null
    ): DataBindingAdapter<T> {
        adapter.apply { onClickListener?.let(::setOnClickListener) }
        recyclerView.adapter = adapter
        return adapter
    }

    fun setAdapterListener(
        recyclerView: RecyclerView,
        @IdRes onClickView: Int? = null,
        onClickListener: (item: RecyclerItem, position: Int) -> Unit
    ): DataBindingAdapter<RecyclerItem> {
        val adapter = DataBindingAdapter.UniversalAdapter(onClickView)
        adapter.setOnClickListener(onClickListener)
        recyclerView.adapter = adapter
        return adapter
    }

    fun scrollToPosition(recyclerView: RecyclerView, position: Int) {
        (recyclerView.layoutManager as? LinearLayoutManager)
            ?.scrollToPositionWithOffset(position, 0)
    }
}