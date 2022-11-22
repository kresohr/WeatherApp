package com.ikresimir.weatherilicic.source

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.ikresimir.weatherilicic.model.DailyList
import com.ikresimir.weatherilicic.model.HourlyList
import com.ikresimir.weatherilicic.model.Location
import com.ikresimir.weatherilicic.model.UserData
import com.ikresimir.weatherilicic.source.local.LocalDao
import com.ikresimir.weatherilicic.source.local.LocalDatabase
import com.ikresimir.weatherilicic.source.remote.OpenWeatherAPIClient
import com.ikresimir.weatherilicic.source.remote.OpenWeatherAPIInterface

/*
     API_KEY would usually be hidden from VCS by using "Gradle Secret Plugin" or other methods.
     This is just for task demonstration purpose.
*/
const val API_KEY="318ce672275d66205dd441034c200da6"
object Repository {
    suspend fun getRemoteData(
        location: MutableLiveData<Location>,
        context: Context,
        latitude: Double,
        longitude: Double
    ): Location? {

        try {
            val apiClient =
                OpenWeatherAPIClient.getInstance().create(OpenWeatherAPIInterface::class.java)
            val result =
                apiClient.getCurrentWeather("onecall?lat=$latitude&lon=$longitude&exclude=minutely,alerts&units=metric&appid=$API_KEY")
            location.value = result
            return result
        } catch (e: Exception) {
            Toast.makeText(context, "Error, check internet connection", Toast.LENGTH_LONG).show()
        }
        return location.value
    }

    suspend fun getData(
        context: Context,
        location: MutableLiveData<Location>,
        latitude: Double,
        longitude: Double
    ) {
        val dao = LocalDatabase.getInstance(context).localDao
        getRemoteData(location, context, latitude, longitude)
        saveToLocalDatabase(dao, location)
    }

    fun localDataExists(context: Context, weatherInUserLocation: MutableLiveData<Location>): Boolean {
        val dao = LocalDatabase.getInstance(context).localDao
        val localData = dao.getLocationData()
        if (localData != null) {
            val location = Location(localData.current,localData.daily.dailyList,localData.hourly.hourlyList,localData.lat,localData.lon,null,null)
            weatherInUserLocation.value = location
            return true
        }
        return false
    }

    private fun saveToLocalDatabase(dao: LocalDao, data: MutableLiveData<Location>) {
        val dailyList = DailyList(data.value!!.daily)
        val hourlyList = HourlyList(data.value!!.hourly)
        val lat = data.value!!.lat
        val lon = data.value!!.lon
        val current = data.value!!.current
        dao.insertLocationData(UserData(0, dailyList, hourlyList, lat, lon, current))
    }
}
