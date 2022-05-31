package cz.cvut.fit.steuejan.wanderscope.app.arch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.steuejan.wanderscope.BR

abstract class DataBindingAdapter<T : RecyclerItem>(
    diffCallback: DiffUtil.ItemCallback<T>,
    @IdRes private val onClickView: Int?
) : ListAdapter<T, DataBindingAdapter.DataBindingViewHolder<T>>(diffCallback) {

    constructor(@IdRes onClickView: Int? = null) : this(RecyclerItem.RecyclerItemlDiffUtil(), onClickView)

    private var onItemClickListener: ((item: T, position: Int) -> Unit)? = null
    private var onItemLongClickListener: ((item: T, position: Int) -> Unit)? = null

    class DataBindingViewHolder<T>(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutId
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        val data = getItem(position)
        holder.bind(data)
        val root = holder.binding.root
        val view = onClickView?.let { root.findViewById(it) ?: root } ?: root
        view.setOnClickListener { onItemClickListener?.invoke(data, position) }
        root.setOnLongClickListener {
            onItemLongClickListener?.invoke(data, position)
            true
        }
    }

    fun setOnClickListener(onClick: (item: T, position: Int) -> Unit) {
        onItemClickListener = onClick
    }

    fun setOnLongClickListener(onLongClick: (item: T, position: Int) -> Unit) {
        onItemLongClickListener = onLongClick
    }

    class UniversalAdapter(@IdRes onClickView: Int? = null) : DataBindingAdapter<RecyclerItem>(onClickView)
}
