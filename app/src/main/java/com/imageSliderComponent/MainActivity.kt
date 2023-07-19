package com.imageSliderComponent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imageSliderComponent.constants.Constants
import com.imageSliderComponent.R
import com.imageSliderComponent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.slider.setList(Constants.slides)

    }
}