package com.ikresimir.weatherilicic.source.remote

import com.ikresimir.weatherilicic.model.Location
import retrofit2.http.GET
import retrofit2.http.Url

interface OpenWeatherAPIInterface {

    @GET
    suspend fun getCurrentWeather(@Url url: String): Location
}