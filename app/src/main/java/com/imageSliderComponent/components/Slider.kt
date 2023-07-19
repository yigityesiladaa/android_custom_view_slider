package com.imageSliderComponent.components

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.imageSliderComponent.R
import com.imageSliderComponent.adapters.ImageSliderAdapter
import com.imageSliderComponent.constants.Constants
import com.imageSliderComponent.databinding.SliderBinding
import com.imageSliderComponent.models.SliderModel
import java.util.*

class Slider @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var _binding: SliderBinding? = null
    private val binding get() = _binding!!

    private var slides = mutableListOf<SliderModel>()
    private var currentIndex = 0

    private var sliderHeight: Int = 0
    private var sliderWidth: Int = 0
    private var cursorSize: Int = 0
    private var cursorMargin: Int = 0

    private var autoPlayInterval = 3000L
    private var isUserInteracting = false
    private var autoPlayHandler: Handler = Handler()
    private val autoPlayRunnable = Runnable {
        binding.viewPager.setCurrentItem(currentIndex + 1, true)
    }

    private var imageSliderAdapter = ImageSliderAdapter()
    private lateinit var sliderCursor: SliderCursor

    init {
        initializeAttributes(context, attrs)
        initView()
        setupViewPager()
    }

    private fun initializeAttributes(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Slider)
        cursorSize = typedArray.getDimensionPixelSize(
            Constants.CURSOR_SIZE_DIMEN, resources.getDimensionPixelSize(R.dimen.default_cursor_size)
        )
        cursorMargin = typedArray.getDimensionPixelSize(
            Constants.CURSOR_MARGIN_DIMEN, resources.getDimensionPixelSize(R.dimen.default_cursor_margin)
        )
        sliderHeight = typedArray.getDimensionPixelSize(
            Constants.SLIDER_HEIGHT_DIMEN, resources.getDimensionPixelSize(R.dimen.default_slider_height)
        )
        sliderWidth = typedArray.getDimensionPixelSize(
            Constants.SLIDER_WIDTH_DIMEN, resources.getDimensionPixelSize(R.dimen.default_slider_width)
        )
        typedArray.recycle()
    }

    private fun initView() {
        _binding = SliderBinding.inflate(LayoutInflater.from(context), this, true)
        sliderCursor = SliderCursor(context, cursorSize, cursorMargin)
        layoutParams = LayoutParams(sliderWidth, sliderHeight)
        sliderCursor.createCursor(binding.cursorLayout, slides)
    }

    private fun setupViewPager() {
        registerPageChangeCallback()
        setOffscreenPageLimit()
        setTouchListener()
    }

    private fun registerPageChangeCallback() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentIndex = position % slides.size
                sliderCursor.updateCursor(currentIndex)
            }
        })
    }

    //TODO: Research
    private fun setOffscreenPageLimit() {
        binding.viewPager.adapter?.let { adapter ->
            val offscreenPageLimit = if (adapter.itemCount > 1) {
                adapter.itemCount - 2
            } else {
                ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            }
            binding.viewPager.offscreenPageLimit = offscreenPageLimit
        }
    }

    //TODO: Refactor By ListSize
    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener() {
        val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                performClick()
                return true
            }
        })

        binding.viewPager.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        isUserInteracting = when (ev.action) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> false
            else -> isUserInteracting
        }
        shouldAutoPlayEnabled()
        return super.dispatchTouchEvent(ev)
    }

    fun setList(slideList: MutableList<SliderModel>) {
        slides.clear()
        slides.addAll(slideList)
        sliderCursor.createCursor(binding.cursorLayout, slides)
        imageSliderAdapter.submitList(slides)
        binding.viewPager.adapter = imageSliderAdapter
        binding.viewPager.offscreenPageLimit = slides.size
        shouldAutoPlayEnabled()
    }

    private fun shouldAutoPlayEnabled() {
        if (!isUserInteracting && slides.size > 1) startAutoPlay() else stopAutoPlay()
    }

    private fun startAutoPlay() {
        stopAutoPlay()
        binding.viewPager.postDelayed(autoPlayRunnable, autoPlayInterval)
    }

    private fun stopAutoPlay() {
        autoPlayHandler.removeCallbacks(autoPlayRunnable)
    }

    //TODO: Research Security
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAutoPlay()
    }
}
