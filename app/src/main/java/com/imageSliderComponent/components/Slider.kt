package com.imageSliderComponent.components

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import com.imageSliderComponent.R
import com.imageSliderComponent.adapters.SliderAdapter
import com.imageSliderComponent.databinding.SliderBinding
import java.util.*

class Slider<T> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var _binding: SliderBinding? = null
    private val binding get() = _binding!!

    private var items = listOf<T>()
    private var currentIndex = 0

    private var sliderHeight: Int = 0
    private var sliderWidth: Int = 0

    private var autoPlayInterval = 3000L
    private var isUserInteracting = false
    private var autoPlayHandler: Handler = Handler()
    private val autoPlayRunnable = Runnable {
        binding.viewPager.setCurrentItem(currentIndex + 1, true)
    }

    private var sliderAdapter: SliderAdapter<T>? = null
    private lateinit var sliderCursor: SliderCursor<T>

    init {
        initializeAttributes(context, attrs)
        initView()
        setupViewPager()
    }

    fun setAdapter(adapter: SliderAdapter<T>) {
        sliderAdapter = adapter
    }

    fun setList(itemList: List<T>) {
        if(sliderAdapter == null){
            throw java.lang.IllegalStateException("You must set an adapter using setAdapter method before calling setList")
        }
        items = itemList
        sliderCursor.createCursor(itemList)
        sliderAdapter?.submitList(itemList)
        binding.viewPager.adapter = sliderAdapter
        binding.viewPager.offscreenPageLimit = itemList.size
        shouldAutoPlayEnabled()
    }

    private fun initializeAttributes(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Slider)
        sliderHeight = typedArray.getDimensionPixelSize(
            R.styleable.Slider_sliderHeight, resources.getDimensionPixelSize(R.dimen.default_slider_height)
        )
        sliderWidth = typedArray.getDimensionPixelSize(
            R.styleable.Slider_sliderWidth, resources.getDimensionPixelSize(R.dimen.default_slider_width)
        )
        typedArray.recycle()
    }

    private fun initView() {
        _binding = SliderBinding.inflate(LayoutInflater.from(context), this, true)
        layoutParams = LayoutParams(sliderWidth, sliderHeight)
        binding.viewPager.adapter = sliderAdapter
        sliderCursor = SliderCursor(context)
        binding.sliderCursor.addView(sliderCursor)
    }

    private fun setupViewPager() {
        registerPageChangeCallback()
        setOffscreenPageLimit()
        setTouchListener()
    }

    private fun registerPageChangeCallback() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentIndex = position % items.size
                sliderCursor.updateCursor(currentIndex)
            }
        })
    }

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

    @Suppress("ClickableViewAccessibility")
    private fun setTouchListener() {
        val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                isUserInteracting = true
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
        isUserInteracting = ev.action != MotionEvent.ACTION_UP && ev.action != MotionEvent.ACTION_CANCEL
        shouldAutoPlayEnabled()
        return super.dispatchTouchEvent(ev)
    }

    private fun shouldAutoPlayEnabled() {
        if (!isUserInteracting && items.size > 1) startAutoPlay() else stopAutoPlay()
    }

    private fun startAutoPlay() {
        stopAutoPlay()
        binding.viewPager.postDelayed(autoPlayRunnable, autoPlayInterval)
    }

    private fun stopAutoPlay() {
        autoPlayHandler.removeCallbacks(autoPlayRunnable)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAutoPlay()
    }
}