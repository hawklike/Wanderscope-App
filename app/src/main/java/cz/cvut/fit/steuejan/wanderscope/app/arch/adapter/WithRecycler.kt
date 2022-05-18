package cz.cvut.fit.steuejan.wanderscope.app.arch.adapter

import androidx.recyclerview.widget.RecyclerView

interface WithRecycler {

    fun <T : RecyclerItem> setCustomAdapter(
        recyclerView: RecyclerView,
        adapter: DataBindingAdapter<T>,
        onClickListener: ((item: T, position: Int) -> Unit)? = null
    ): DataBindingAdapter<T> {
        adapter.apply {
            onClickListener?.let {
                setOnClickListener(it)
            }
        }
        recyclerView.adapter = adapter
        return adapter
    }

    fun setAdapterListener(
        recyclerView: RecyclerView,
        onClickListener: ((item: RecyclerItem, position: Int) -> Unit)
    ): DataBindingAdapter<RecyclerItem> {
        val adapter = DataBindingAdapter.UniversalAdapter()
        adapter.apply {
            setOnClickListener(onClickListener)
        }
        recyclerView.adapter = adapter
        return adapter
    }
}