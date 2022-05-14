package cz.cvut.fit.steuejan.wanderscope.app.binding

import android.view.ViewGroup
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

@BindingAdapter(
    value = ["marginEndWhenOneItem", "marginEndRes", "marginEndNormalRes"],
    requireAll = true
)
fun <T : RecyclerItem> RecyclerView.countItemsAndSetMargin(
    recyclerItems: List<T>?,
    endMargin: Float,
    endMarginNormal: Float
) {
    recyclerItems ?: return
    val marginParams = this.layoutParams

    if (marginParams is ViewGroup.MarginLayoutParams) {
        marginParams.rightMargin = if (recyclerItems.size == 1) {
            endMargin.toInt()
        } else {
            endMarginNormal.toInt()
        }
        requestLayout()
    }
}