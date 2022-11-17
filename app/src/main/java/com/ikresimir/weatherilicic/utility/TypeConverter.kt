package com.ikresimir.weatherilicic.utility

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ikresimir.weatherilicic.model.Current
import com.ikresimir.weatherilicic.model.CurrentList
import com.ikresimir.weatherilicic.model.DailyList
import com.ikresimir.weatherilicic.model.HourlyList

class TypeConverter {

    @TypeConverter
    fun fromDailyToJSON(dailyList: DailyList): String {
        return Gson().toJson(dailyList)
    }
    @TypeConverter
    fun fromJSONToDaily(json: String): DailyList {
        return Gson().fromJson(json,DailyList::class.java)
    }

    @TypeConverter
    fun fromHourlyToJSON(hourlyList: HourlyList): String {
        return Gson().toJson(hourlyList)
    }

    @TypeConverter
    fun fromJSONToHourly(json: String): HourlyList {
        return Gson().fromJson(json,HourlyList::class.java)
    }

    @TypeConverter
    fun fromCurrentToJSON(current: Current): String {
        return Gson().toJson(current)
    }

    @TypeConverter
    fun fromJSONToCurrent(json: String): Current {
        return Gson().fromJson(json,Current::class.java)
    }

}