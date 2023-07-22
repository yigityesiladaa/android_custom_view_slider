package com.customViewSlider.components

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.customViewSlider.R
import com.customViewSlider.constants.Constants.SELECTED_CURSOR_SHAPE
import com.customViewSlider.constants.Constants.UNSELECTED_CURSOR_SHAPE

class SliderCursor<T> @JvmOverloads constructor(
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

    fun createCursor(slides: List<T>) {
        cursorViews.clear()
        removeAllViews()

        for (index in slides.indices) {
            val cursorView = ImageView(context).apply {
                layoutParams = LayoutParams(cursorSize, cursorSize).apply {
                    marginEnd = cursorMargin
                }
                setImageResource(if (index == 0) SELECTED_CURSOR_SHAPE else UNSELECTED_CURSOR_SHAPE)
            }
            cursorViews.add(cursorView)
            addView(cursorView)
        }
    }

    fun updateCursor(position: Int) {
        cursorViews.forEachIndexed { index, cursorView ->
            cursorView.setImageResource(if (index == position) SELECTED_CURSOR_SHAPE else UNSELECTED_CURSOR_SHAPE)
        }
    }
}
