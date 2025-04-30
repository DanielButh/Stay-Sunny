package com.example.staysunny.network

import android.util.Log
import com.example.staysunny.core.RetrofitInstance
import com.example.staysunny.core.WeatherAPI
import com.example.staysunny.model.Weather

class WeatherRepository {
    private var retrofit = RetrofitInstance.getRetrofit().create(WeatherAPI::class.java)

    suspend fun getWeatherDetail(coordinates: String): Weather? {
        val response = retrofit.getWeatherInfo("c5d316da89f8417aae562221251904", coordinates)
        Log.i("RESPONSE", response.body().toString())

        return response.body()
    }
}