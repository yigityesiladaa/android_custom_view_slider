package com.imageSliderComponent.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imageSliderComponent.constants.Constants
import com.imageSliderComponent.models.SliderModel

class HomeViewModel : ViewModel() {
    var sliderList = MutableLiveData<MutableList<SliderModel>>()

    fun loadSlider(){
        sliderList.postValue(Constants.slides)
    }
}