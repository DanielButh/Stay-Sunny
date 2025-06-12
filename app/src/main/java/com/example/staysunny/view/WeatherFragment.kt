package com.example.staysunny.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.staysunny.R
import com.example.staysunny.core.LocationProvider
import com.example.staysunny.model.Weather
import com.example.staysunny.utils.FragmentCommunicator
import com.example.staysunny.databinding.FragmentWeatherBinding
import com.example.staysunny.view.HomeActivity
import com.example.staysunny.view.OnboardingActivity
import com.example.staysunny.viewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null

    private val binding get() = _binding!!
    private val viewModel by viewModels<WeatherViewModel>()
    private lateinit var communicator: FragmentCommunicator
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fineLocationGranted || coarseLocationGranted) {
            getUserLocation(locationCoordinatesCallback)
        }
    }
    private val locationCoordinatesCallback: (String?) -> Unit = { coordinates ->
        if (coordinates != null) {
            Log.d("WeatherFragment", "Coordenadas obtenidas: $coordinates")
            viewModel.getWeatherDetail(coordinates)
        } else {
            Log.e("WeatherFragment", "No se pudieron obtener las coordenadas.")
            Toast.makeText(requireContext(), "No se pudo obtener la ubicación para el clima.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        communicator = requireActivity() as HomeActivity
        setupView()
        setupObservers()
        return binding.root
    }

    fun setupView() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("WeatherFragment", "Permiso ya concedido en setupView.")
            getUserLocation(locationCoordinatesCallback)
        } else {
            Log.d("WeatherFragment", "Solicitando permiso de ubicación desde setupView.")
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    fun getUserLocation(onResult: (String?) -> Unit) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lifecycleScope.launch {
                try {
                    val location = LocationProvider.getInstance(requireContext()).getCurrentLocation()
                    location?.let {
                        val coordinatesString = "${it.latitude},${it.longitude}"
                        Log.i("LOCATION", "Location: $coordinatesString")
                        onResult(coordinatesString)
                    } ?: run {
                        Log.e("LOCATION", "La ubicación obtenida es nula.")
                        onResult(null)
                    }
                } catch (e: Exception) {
                    Log.e("LOCATION", "Excepción al obtener la ubicación: ${e.message}", e)
                    onResult(null)
                }
            }
        } else {
            Log.w("LOCATION_HELPER", "Permiso de ubicación no concedido al intentar obtener ubicación.")
            onResult(null)
        }
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
        binding.tvDate.text = weather.location.localtime.split(" ")[1]
        binding.tvCelsius.text = weather.current.tempC.toString() + " °C"
        binding.tvGreeting.text = if (weather.current.isDay == 1) "Good morning" else "Good night"
        binding.tvSunriseInfo.text = weather.location.localtime.split(" ")[1] + " hrs"
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