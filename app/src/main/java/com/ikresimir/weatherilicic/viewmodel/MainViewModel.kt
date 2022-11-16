package com.ikresimir.weatherilicic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikresimir.weatherilicic.model.Location
import com.ikresimir.weatherilicic.source.Repository
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {

    private val weatherInUserLocation: MutableLiveData<Location> by lazy{
        MutableLiveData<Location>()
    }

    fun getWeather(): MutableLiveData<Location>{
        viewModelScope.launch {
            try{
                Repository.getRemoteData(weatherInUserLocation)
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
}