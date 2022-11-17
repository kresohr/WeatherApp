package com.ikresimir.weatherilicic.viewmodel

import android.app.Application
import android.location.Geocoder
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ikresimir.weatherilicic.model.Location
import com.ikresimir.weatherilicic.source.Repository
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val weatherInUserLocation: MutableLiveData<Location> by lazy{
        MutableLiveData<Location>()
    }

    fun getWeather(): MutableLiveData<Location>{
        viewModelScope.launch {
            try{
                Repository.getData(getApplication(), weatherInUserLocation)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        return weatherInUserLocation
    }

    // Can be improved to check based on the season (Daylight is prone to change based on season)
    fun checkIfNight(): Boolean{
        val test = weatherInUserLocation.value?.current?.dt
        val sdf= java.text.SimpleDateFormat("HH")
        val date = Date((test?.times(1000L)!!))
        val hour = sdf.format(date).toInt()
        return hour>16 || hour<7
    }

    fun getCityName(): String{
        if (weatherInUserLocation.value != null){
            val lat = weatherInUserLocation.value!!.lat
            val lon = weatherInUserLocation.value!!.lon
            var cityName: String?
            val geoCoder = Geocoder(getApplication(), Locale.getDefault())
            val address = geoCoder.getFromLocation(lat,lon,1)
            cityName = address[0].adminArea
            if (cityName == null){
                cityName = address[0].locality
                if (cityName == null){
                    cityName = address[0].subAdminArea
                }
            }
            return cityName
        }
        else{
            Toast.makeText(getApplication(),"Can't locate city, please use search", Toast.LENGTH_LONG).show()
            return "Unknown location"
        }

    }
}