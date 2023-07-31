package com.customViewSlider.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.customViewSlider.constants.Constants
import com.customViewSlider.models.SliderModel

class HomeViewModel : ViewModel() {
    var sliderList = MutableLiveData<List<SliderModel>>()

    fun loadSlider(){
        sliderList.postValue(Constants.slides)
    }

}