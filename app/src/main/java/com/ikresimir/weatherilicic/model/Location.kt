package com.ikresimir.weatherilicic.model

data class Location(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    @Transient val timezone: String,
    @Transient val timezone_offset: Int
)