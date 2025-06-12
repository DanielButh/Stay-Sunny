package com.example.staysunny.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staysunny.model.Weather
import com.example.staysunny.network.WeatherRepository
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
private val repository: WeatherRepository
): ViewModel() {

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    private val _weatherInfo = MutableLiveData<Weather>()
    val weatherInfo: LiveData<Weather>
        get() = _weatherInfo

    fun getWeatherDetail(coordinates: String) {
        _loaderState.value = true
        viewModelScope.launch {
            val response = repository.getWeatherDetail(coordinates)
            _loaderState.value = false
            response?.let {
                _weatherInfo.value = it
            } ?: run {
                Log.e("API ERROR", "NO SE PUDO COMPLETAR LA PETICION")
            }
        }
    }
}
