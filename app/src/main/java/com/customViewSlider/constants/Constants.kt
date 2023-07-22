package com.customViewSlider.constants

import com.customViewSlider.R
import com.customViewSlider.models.SliderModel

object Constants {

    val slides = listOf(
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
}