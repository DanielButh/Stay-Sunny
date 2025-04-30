package com.example.staysunny.model

import com.google.gson.annotations.SerializedName

data class Weather(
    val location: Location,
    val current: Current
) {
    data class Location(
        val name: String,
        val region: String,
        val country: String,
        val lat: Float,
        val lon: Float,
        val localtime: String
    )

    data class Current(
        @SerializedName("last_updated") val lastUpdated: String,
        @SerializedName("temp_c") val tempC: Float,
        @SerializedName("is_day") val isDay: Int,
        val condition: Condition,
        @SerializedName("wind_mph")val windMph: Float
    )

    data class Condition(
        val text: String,
        val icon: String,
        val code: Int
    )

}

