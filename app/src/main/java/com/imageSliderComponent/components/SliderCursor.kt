package com.imageSliderComponent.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.imageSliderComponent.R
import com.imageSliderComponent.constants.Constants
import com.imageSliderComponent.databinding.SliderCursorBinding
import com.imageSliderComponent.models.SliderModel

class SliderCursor @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var cursorSize: Int = 0
    private var cursorMargin: Int = 0
    private val cursorViews = mutableListOf<ImageView>()

    init {
        initializeCursorAttributes(context, attrs)
    }

    private fun initializeCursorAttributes(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SliderCursor)
        cursorSize = typedArray.getDimensionPixelSize(
            R.styleable.SliderCursor_cursorSize, resources.getDimensionPixelSize(R.dimen.default_cursor_size)
        )
        cursorMargin = typedArray.getDimensionPixelSize(
            R.styleable.SliderCursor_cursorMargin, resources.getDimensionPixelSize(R.dimen.default_cursor_margin)
        )

        typedArray.recycle()
    }

    fun createCursor(slides: List<SliderModel>) {
        cursorViews.clear()
        removeAllViews()

        for (index in slides.indices) {
            val cursorView = ImageView(context).apply {
                layoutParams = LayoutParams(cursorSize, cursorSize).apply {
                    marginEnd = cursorMargin
                }
                setImageResource(if (index == 0) Constants.SELECTED_CURSOR_SHAPE else Constants.UNSELECTED_CURSOR_SHAPE)
            }
            cursorViews.add(cursorView)
            addView(cursorView)
        }
    }

    fun updateCursor(position: Int) {
        for (index in cursorViews.indices) {
            cursorViews[index].setImageResource(
                if (index == position) Constants.SELECTED_CURSOR_SHAPE else Constants.UNSELECTED_CURSOR_SHAPE
            )
        }
    }
}
