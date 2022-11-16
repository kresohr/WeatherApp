package com.ikresimir.weatherilicic.source

import androidx.lifecycle.MutableLiveData
import com.ikresimir.weatherilicic.model.Location
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
    suspend fun getRemoteData(location: MutableLiveData<Location>) {
        val apiClient = OpenWeatherAPIClient.getInstance().create(OpenWeatherAPIInterface::class.java)
        val result = apiClient.getCurrentWeather("onecall?lat=45.8426414&lon=15.9622315&exclude=minutely,alerts&units=metric&appid="+ API_KEY)
        location.value = result
    }

    fun saveToLocalDatabase(){}

    fun getLocalData(){}

    // This function will compare local and remote data, if same, fetch local
    fun compareData(){

    }

}