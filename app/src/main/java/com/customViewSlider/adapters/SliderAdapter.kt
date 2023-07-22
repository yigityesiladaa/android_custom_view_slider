package com.customViewSlider.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SliderAdapter<T>(private val layoutResId: Int, private val binder: SliderBinder<T>) :
    RecyclerView.Adapter<SliderAdapter<T>.SliderViewHolder>() {

    private var items: List<T> = emptyList()

    fun submitList(list: List<T>) {
        items = list
        notifyItemRangeInserted(0, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layoutResId, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val item = items[position % items.size]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return if (items.size > 1) Int.MAX_VALUE else items.size
    }

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: T) {
            binder.bind(itemView, item)
        }
    }

    interface SliderBinder<T> {
        fun bind(view: View, item: T)
    }
}
