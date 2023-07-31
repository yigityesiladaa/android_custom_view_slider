package com.customViewSlider.components

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.customViewSlider.R
import com.customViewSlider.adapters.BaseSliderAdapter
import com.customViewSlider.databinding.SliderBinding
import java.util.*

class Slider<T : Any> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), LifecycleEventObserver {

    private var _binding: SliderBinding? = null
    private val binding get() = _binding!!

    private var items = listOf<T>()
    private var currentIndex = 0

    private var sliderHeight: Int = 0
    private var sliderWidth: Int = 0

    private var autoPlayInterval = 3000L
    private var isUserInteracting = false
    private var autoPlayHandler: Handler = Handler(Looper.getMainLooper())
    private var isAutoPlayHandlerPosted = false

    private val autoPlayRunnable = Runnable {
        binding.viewPager.setCurrentItem((currentIndex + 1) % items.size, true)
        shouldAutoPlayEnabled()
    }

    private var sliderAdapter: BaseSliderAdapter<T, *>? = null
    private lateinit var sliderCursor: SliderCursor

    init {
        initializeAttributes(context, attrs)
        initView()
        setupViewPager()
    }

    fun setAdapter(adapter: BaseSliderAdapter<T, *>) {
        sliderAdapter = adapter
    }

    fun setList(itemList: List<T>) {
        sliderAdapter?.let { adapter ->
            items = itemList
            sliderCursor.createCursor(itemList.size)
            adapter.submitList(itemList)
            binding.viewPager.run {
                this.adapter = adapter
                offscreenPageLimit = itemList.size
            }
            shouldAutoPlayEnabled()
        } ?: throw IllegalStateException("You must set an adapter using setAdapter method before calling setList")
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
        sliderCursor = SliderCursor(context)
        binding.viewPager.adapter = sliderAdapter
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
        binding.run {
            viewPager.adapter?.let { adapter ->
                val offscreenPageLimit = if (adapter.itemCount > 1) {
                    adapter.itemCount - 2
                } else {
                    ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
                }
                viewPager.offscreenPageLimit = offscreenPageLimit
            }
        }
    }

    @Suppress("ClickableViewAccessibility")
    private fun setTouchListener() {
        val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                isUserInteracting = true
                return true
            }
        })

        binding.viewPager.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val isInteractingNow = ev.action != MotionEvent.ACTION_UP && ev.action != MotionEvent.ACTION_CANCEL
        if (isUserInteracting != isInteractingNow) {
            isUserInteracting = isInteractingNow
            shouldAutoPlayEnabled()
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun shouldAutoPlayEnabled() {
        if (!isUserInteracting && items.size > 1) {
            if (isAutoPlayHandlerPosted) {
                stopAutoPlay()
            }
            startAutoPlay()
        } else {
            if (isAutoPlayHandlerPosted) {
                stopAutoPlay()
            }
        }
    }

    private fun startAutoPlay() {
        stopAutoPlay()
        autoPlayHandler.postDelayed(autoPlayRunnable, autoPlayInterval)
        isAutoPlayHandlerPosted = true
    }

    private fun stopAutoPlay() {
        autoPlayHandler.removeCallbacks(autoPlayRunnable)
        isAutoPlayHandlerPosted = false
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                stopAutoPlay()
                source.lifecycle.removeObserver(this)
            }
            Lifecycle.Event.ON_RESUME -> {
                source.lifecycle.addObserver(this)
            }
            Lifecycle.Event.ON_DESTROY -> {
                stopAutoPlay()
                source.lifecycle.removeObserver(this)
                _binding = null
            }
            else -> {}
        }
    }
}
