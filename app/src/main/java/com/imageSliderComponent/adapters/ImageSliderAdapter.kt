package com.imageSliderComponent.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imageSliderComponent.R
import com.imageSliderComponent.models.SliderModel

class ImageSliderAdapter : RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {
    private var slides = mutableListOf<SliderModel>()

    //TODO: Don't User Int.MAX_VALUE
    override fun getItemCount(): Int {
        return if (slides.size > 1) {
            Int.MAX_VALUE
        } else {
            slides.size
        }
    }

    inner class ImageSliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imgBackground)
        private val txtData: TextView = itemView.findViewById(R.id.txtData)

        fun bind(slide: SliderModel) {
            txtData.text = slide.text
            Glide.with(imageView).load(slide.url).into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slider_image, parent, false)
        return ImageSliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        holder.bind(slides[position % slides.size])
    }

    fun submitList(list: MutableList<SliderModel>) {
        slides.clear()
        slides.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }
}


