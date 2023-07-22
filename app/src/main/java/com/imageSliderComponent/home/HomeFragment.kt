package com.imageSliderComponent.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.imageSliderComponent.R
import com.imageSliderComponent.adapters.SliderAdapter
import com.imageSliderComponent.databinding.FragmentHomeBinding
import com.imageSliderComponent.models.SliderModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sliderAdapter: SliderAdapter<SliderModel>
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSliderAdapter()
        listenEvents()
        homeViewModel.loadSlider()
    }

    private fun setupSliderAdapter() {
        val layoutResId = R.layout.item_slider_image
        sliderAdapter = SliderAdapter(layoutResId, object : SliderAdapter.SliderBinder<SliderModel> {
            override fun bind(view: View, item: SliderModel) {
                val txtData = view.findViewById<TextView>(R.id.txtData)
                val imgBackground = view.findViewById<ImageView>(R.id.imgBackground)
                txtData.text = item.text
                Glide.with(requireContext()).load(item.url).into(imgBackground)
            }
        })
        binding.slider.setAdapter(sliderAdapter)
    }

    private fun listenEvents(){
        homeViewModel.sliderList.observe(viewLifecycleOwner){
            binding.slider.setList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}