package com.customViewSlider.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.customViewSlider.R
import com.customViewSlider.models.SliderModel

class ImageSliderAdapter: BaseSliderAdapter<SliderModel,ImageSliderAdapter.ImageSliderViewHolder>() {
    private var items: List<SliderModel> = listOf()

    override fun submitList(list: List<SliderModel>) {
        items = list
        notifyItemRangeInserted(0, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_slider_image, parent, false)
        return ImageSliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        val item = items[position % items.size]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return if (items.size > 1) Int.MAX_VALUE else items.size
    }

    inner class ImageSliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SliderModel) {
            val txtData = itemView.findViewById<TextView>(R.id.txtData)
            val imgBackground = itemView.findViewById<ImageView>(R.id.imgBackground)
            txtData.text = item.text
            Glide.with(itemView).load(item.url).into(imgBackground)
        }
    }


}
