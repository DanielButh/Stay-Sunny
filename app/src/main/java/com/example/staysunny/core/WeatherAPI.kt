package com.example.staysunny.core

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("current.json") //Este es el endpoint que pide el clima actual en WeatherAPI.
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,  //Tu clave de API que te da weatherapi.com.
        @Query("q") location: String   // La ciudad o coordenada que quieres buscar.
    ): Response<Weather>   //Respuesta del JSON.


}