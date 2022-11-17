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
    //TO DO: Create dynamic call with arguments on API.
    //Location will be retrieved from user through permission
    suspend fun getRemoteData(location: MutableLiveData<Location>, context: Context): Location? {

        try {
            val apiClient = OpenWeatherAPIClient.getInstance().create(OpenWeatherAPIInterface::class.java)
            val result = apiClient.getCurrentWeather("onecall?lat=45.8426414&lon=15.9622315&exclude=minutely,alerts&units=metric&appid="+ API_KEY)
            location.value = result
            return result
        } catch (e: Exception){
            Toast.makeText(context,"Error, check internet connection", Toast.LENGTH_LONG).show()
        }
        return location.value
    }

    suspend fun getData(context: Context, location: MutableLiveData<Location> ){
        val dao = LocalDatabase.getInstance(context).localDao

        if (getLocalData(dao) == null){
            getRemoteData(location, context)
            saveToLocalDatabase(dao, location)
        }
        else{
            location.value = getLocalData(dao) //Temporarily populate with data from local db

            // Fetch latest data from API and compare with local data
            val remoteData = getRemoteData(location, context)
            if(checkIfLocalAndRemoteEqual(dao, location)){
                location.value = getLocalData(dao)
            }
            else{
                location.value = remoteData
                saveToLocalDatabase(dao, location) //Since data is not equal, we store it in local db
            }
        }
    }

    fun getLocalData(dao: LocalDao): Location?{
        val localData = dao.getLocationData()
        if (localData != null){
           return Location(localData.current,localData.daily.dailyList,localData.hourly.hourlyList,localData.lat,localData.lon,null,null)
        }
        return null
    }

    fun saveToLocalDatabase(dao: LocalDao, data: MutableLiveData<Location>) {
        val dailyList = DailyList(data.value!!.daily)
        val hourlyList = HourlyList(data.value!!.hourly)
        val lat = data.value!!.lat
        val lon = data.value!!.lon
        val current = data.value!!.current
        dao.insertLocationData(UserData(0,dailyList,hourlyList,lat,lon,current))
    }

    fun checkIfLocalAndRemoteEqual(dao: LocalDao, data: MutableLiveData<Location>): Boolean{
        var isDataEqual = true
        try {
            if (dao.getLocationData().hourly.hourlyList[0].dt == data.value!!.hourly[0].dt){
                println("Data equal")
                return isDataEqual
            }
            else{
                println("Data NOT equal")
                return !isDataEqual
            }
        }catch (e: Exception){
            e.printStackTrace()
            return !isDataEqual
        }
    }

}