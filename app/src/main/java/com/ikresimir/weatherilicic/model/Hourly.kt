package com.ikresimir.weatherilicic.model

data class Hourly(
    @Transient val clouds: Int,
    @Transient val dew_point: Double,
    val dt: Int,
    @Transient val feels_like: Double,
    @Transient val humidity: Int,
    @Transient val pop: Double,
    @Transient val pressure: Int,
    val temp: Double,
    @Transient val uvi: Double,
    @Transient val visibility: Int,
    val weather: List<Weather>,
    @Transient val wind_deg: Int,
    @Transient val wind_gust: Double,
    @Transient val wind_speed: Double
)