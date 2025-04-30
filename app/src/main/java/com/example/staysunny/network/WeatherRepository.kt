package com.example.staysunny.network

import android.util.Log
import com.example.staysunny.core.RetrofitInstance
import com.example.staysunny.core.WeatherAPI
import com.example.staysunny.model.Weather

class WeatherRepository {
    private val retrofit = RetrofitInstance.getRetrofit().create(WeatherAPI::class.java)

    suspend fun getWeatherDetail(coordinates: String): Weather? {
        val response = retrofit.getWeatherInfo("dc798fb4f525444bbad62116251904", coordinates)
        Log.i("RESPONSE", response.body().toString())

        return response.body()
    }
}