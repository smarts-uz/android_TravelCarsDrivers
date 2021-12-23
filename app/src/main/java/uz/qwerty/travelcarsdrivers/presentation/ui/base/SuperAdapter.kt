package uz.qwerty.travelcarsdrivers.presentation.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


abstract class SuperAdapter<T>(
    @LayoutRes val restId: Int,
    diffUtil: DiffUtil.ItemCallback<T>
) : ListAdapter<T, SuperAdapter<T>.MyHolder>(diffUtil) {

    constructor(
        restId: Int,
        areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
        areContentsTheSame: (oldItem: T, newItem: T) -> Boolean
    ) : this(restId, object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return areItemsTheSame.invoke(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return areContentsTheSame(oldItem, newItem)
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(restId, parent, false)
        return MyHolder(root)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindHolder()
    }


    inner class MyHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun bindHolder() {
            bind(getItem(adapterPosition), view, adapterPosition)
        }
    }

    abstract fun bind(t: T, view: View, adapterPosition: Int)

    lateinit var listenerType: (t: T) -> Unit
    lateinit var listenerId: (t: Int) -> Unit
    lateinit var listenerView: (t: View) -> Unit

    fun myListener(listener: (t: T) -> Unit) {
        this.listenerType = listener
    }


}
