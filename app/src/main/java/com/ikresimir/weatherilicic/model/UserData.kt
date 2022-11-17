package com.ikresimir.weatherilicic.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ikresimir.weatherilicic.utility.TypeConverter

@TypeConverters(value = [TypeConverter::class])
@Entity (tableName = "user_data")
data class UserData (
    @PrimaryKey(autoGenerate = false) val id: Int,

    val daily: DailyList,
    val hourly: HourlyList,
    val lat: Double,
    val lon: Double,
    val current: Current
)

