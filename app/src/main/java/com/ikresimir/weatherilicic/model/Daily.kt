package com.ikresimir.weatherilicic.model


data class Daily(
    @Transient val clouds: Int,
    @Transient val dew_point: Double,
    val dt: Int,
    @Transient val feels_like: FeelsLike,
    @Transient val humidity: Int,
    @Transient val moon_phase: Double,
    @Transient val moonrise: Int,
    @Transient val moonset: Int,
    @Transient val pop: Double,
    @Transient val pressure: Int,
    @Transient val rain: Double,
    @Transient val sunrise: Int,
    @Transient val sunset: Int,
    val temp: Temp,
    @Transient val uvi: Double,
    val weather: List<Weather>,
    @Transient val wind_deg: Int,
    @Transient val wind_gust: Double,
    @Transient val wind_speed: Double
)