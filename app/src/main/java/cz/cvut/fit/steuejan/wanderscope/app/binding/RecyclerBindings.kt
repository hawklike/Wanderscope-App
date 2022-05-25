package cz.cvut.fit.steuejan.wanderscope.app.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.DataBindingAdapter
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem

@BindingAdapter("recyclerItems")
fun <T : RecyclerItem> RecyclerView.bindRecyclerItems(recyclerItems: List<T>?) {
    setAdapterIfNull()
    @Suppress("UNCHECKED_CAST")
    (adapter as DataBindingAdapter<T>).submitList(recyclerItems)
}

private fun RecyclerView.setAdapterIfNull() {
    if (this.adapter == null) {
        this.adapter = DataBindingAdapter.UniversalAdapter()
    }
}