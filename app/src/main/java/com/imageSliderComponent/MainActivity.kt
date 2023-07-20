package com.imageSliderComponent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imageSliderComponent.home.HomeFragment

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        Since it will be a single fragment for example purposes, I performed the process in this way.
        If I'm going to handle more than one fragment, I would prefer to use Navigation Component.
         */
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commit()
        }

    }
}