package com.ikresimir.weatherilicic.source.remote
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object OpenWeatherAPIClient {

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/3.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }




}