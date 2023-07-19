package com.imageSliderComponent.components

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.imageSliderComponent.constants.Constants
import com.imageSliderComponent.models.SliderModel

class SliderCursor(
    private val context: Context,
    private val cursorSize: Int,
    private val cursorMargin: Int
) {

    private val cursorViews = mutableListOf<ImageView>()

    fun createCursor(container: ViewGroup, slides: List<SliderModel>) {
        cursorViews.clear()
        container.removeAllViews()

        for (index in slides.indices) {
            val cursorView = ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(cursorSize, cursorSize).apply {
                    marginEnd = cursorMargin
                }
                setImageResource(if (index == 0) Constants.SELECTED_CURSOR_SHAPE else Constants.UNSELECTED_CURSOR_SHAPE)
            }
            cursorViews.add(cursorView)
            container.addView(cursorView)
        }
    }

    //TODO: Update
    fun updateCursor(position: Int) {
        for (index in cursorViews.indices) {
            cursorViews[index].setImageResource(
                if (index == position) Constants.SELECTED_CURSOR_SHAPE else Constants.UNSELECTED_CURSOR_SHAPE
            )
        }
    }
}
