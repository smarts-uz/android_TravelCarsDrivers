package uz.qwerty.travelcarsdrivers.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class SuperListAdapter<T, V : ViewBinding>(
        diffUtil: DiffUtil.ItemCallback<T>
) : ListAdapter<T, SuperListAdapter<T, V>.MyHolder>(diffUtil) {

    constructor(
            areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
            areContentsTheSame: (oldItem: T, newItem: T) -> Boolean
    ) : this(object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return areContentsTheSame(oldItem, newItem)
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = findBinding(inflater, parent)
        return MyHolder(binding)
    }

    abstract fun findBinding(inflater: LayoutInflater, parent: ViewGroup): V

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindHolder()
    }


    inner class MyHolder(val bindingItem: V) : RecyclerView.ViewHolder(bindingItem.root) {
        fun bindHolder() {
            bind(getItem(adapterPosition), bindingItem, adapterPosition)
        }
    }

    abstract fun bind(t: T, bindingItem: V, adapterPosition: Int)
}