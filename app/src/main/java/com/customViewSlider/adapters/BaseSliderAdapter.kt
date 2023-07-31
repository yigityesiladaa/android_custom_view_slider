package com.customViewSlider.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseSliderAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    abstract fun submitList(list: List<T>)
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
    abstract override fun onBindViewHolder(holder: VH, position: Int)
    abstract override fun getItemCount(): Int
}
