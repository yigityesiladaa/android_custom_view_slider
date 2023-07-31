package com.customViewSlider.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.customViewSlider.R
import com.customViewSlider.adapters.ImageSliderAdapter
import com.customViewSlider.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private val sliderAdapter = ImageSliderAdapter()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupSlider()
        setupClickListeners()
        homeViewModel.loadSlider()
        listenToSliderChanges()
    }

    private fun setupSlider() {
        binding.slider.apply {
            setAdapter(sliderAdapter)
            viewLifecycleOwner.lifecycle.addObserver(this)
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener { navController.popBackStack() }
        binding.btnGoToNextPage.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_testFragment)
        }
    }

    private fun listenToSliderChanges() {
        homeViewModel.sliderList.observe(viewLifecycleOwner) { sliderList ->
            binding.slider.setList(sliderList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
