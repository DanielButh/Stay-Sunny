package com.example.staysunny.network

import android.util.Log
import com.example.staysunny.core.RetrofitInstance
import com.example.staysunny.core.WeatherAPI
import com.example.staysunny.model.Weather

class WeatherRepository {
    private var retrofit = RetrofitInstance.getRetrofit().create(WeatherAPI::class.java)

    suspend fun getCurrentWeather(): Weather? {
        val reponse = retrofit.getCurrentWeather("ccbb8f761b5b493cb1b232630252904", "19.330410742165437, -99.21020365058398")
        Log.i("RESPONSE", reponse.body().toString())

        return reponse.body()
    }
}