package com.imageSliderComponent.constants

import com.imageSliderComponent.R
import com.imageSliderComponent.models.SliderModel

object Constants {

    val slides = mutableListOf(
        SliderModel(
            url = "https://i.hizliresim.com/l0608s4.jpg",
            text = "İSTEĞİNE GÖRE SİGORTA PAYCELL'DE",
        ),

        SliderModel(
            url = "https://i.hizliresim.com/ng5wx1n.jpg",
            text = "İSTEĞİNE GÖRE KREDİ PAYCELL'DE",
        ),

        SliderModel(
            url = "https://i.hizliresim.com/l1qt142.jpg",
            text = "İSTEĞİNE GÖRE YATIRIM PAYCELL'DE",
        )
    )

    const val SELECTED_CURSOR_SHAPE = R.drawable.selected_cursor
    const val UNSELECTED_CURSOR_SHAPE = R.drawable.unselected_cursor
    const val CURSOR_SIZE_DIMEN = R.styleable.SliderCursor_cursorSize
    const val CURSOR_MARGIN_DIMEN = R.styleable.SliderCursor_cursorMargin
    const val SLIDER_HEIGHT_DIMEN = R.styleable.Slider_sliderHeight
    const val SLIDER_WIDTH_DIMEN = R.styleable.Slider_sliderWidth
}