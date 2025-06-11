package com.example.staysunny.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.staysunny.R
import com.example.staysunny.model.Weather
import com.example.staysunny.utils.FragmentCommunicator
import com.example.staysunny.databinding.FragmentWeatherBinding
import com.example.staysunny.view.HomeActivity
import com.example.staysunny.view.OnboardingActivity
import com.example.staysunny.viewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by viewModels<WeatherViewModel>()
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        communicator = requireActivity() as HomeActivity
        setupView()
        return binding.root
    }

    fun setupView() {
        setupObservers()
        val coordinates = getUserCoordinates()
        viewModel.getWeatherDetail(coordinates)
    }

    fun setupObservers() {
        viewModel.weatherInfo.observe(viewLifecycleOwner) { weather ->
            showWeatherInfo(weather)
        }

        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }
    }

    fun showWeatherInfo(weather: Weather) {
        binding.tvCity.text = weather.location.name
        binding.tvDate.text = if (weather.current.isDay ==1) weather.location.localtime.split(" ")[1] + " a.m" else weather.location.localtime.split(" ")[1] + " p.m"
        binding.tvCelsius.text = weather.current.tempC.toString() + " °C"
        binding.tvGreeting.text = if (weather.current.isDay == 1) "Good morning" else "Good night"
        binding.tvSunriseInfo.text = if (weather.current.isDay ==1) weather.location.localtime.split(" ")[1] + " a.m" else weather.location.localtime.split(" ")[1] + " p.m"
        binding.tvWindInfo.text = weather.current.windMph.toString() + " m/s"
        binding.tvTempratureInfo.text = weather.current.tempC.toString() + " °C"

        Glide.with(this)
            .load("https:" + weather.current.condition.icon)
            .placeholder(R.drawable.pending_24px)
            .error(R.drawable.sentiment_sad_24px)
            .into(binding.ivWeather)
    }

    fun getUserCoordinates(): String {
        return "19.32871829633027, -99.16549389549148"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}