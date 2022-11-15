package com.ikresimir.weatherilicic.source.remote
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object OpenWeatherAPIClient {

    // https://api.openweathermap.org/data/3.0/onecall?lat=45.815010&lon=15.981919&exclude=minutely,hourly.pressure,alerts,hourly.pressure,hourly.humidity,hourly.dew_point,hourly.uvi,hourly.clouds,hourly.visibility,hourly.wind_deg,hourly.wind_gust,weather.main,weather.description,weather.icon&units=metric&appid=318ce672275d66205dd441034c200da6

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/3.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }








}