package com.ikresimir.weatherilicic.viewmodel

import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.ikresimir.weatherilicic.model.Location
import com.ikresimir.weatherilicic.source.Repository
import com.ikresimir.weatherilicic.utility.BadWeatherNotificationWorker
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val weatherInUserLocation: MutableLiveData<Location> by lazy{
        MutableLiveData<Location>()
    }


    var permissionDialogShown = false

    fun getWeather(): MutableLiveData<Location>{
        return weatherInUserLocation
    }

    fun requestWeatherData(latitude: Double, longitude: Double){
        viewModelScope.launch {
            try{
                Repository.getData(getApplication(), weatherInUserLocation, latitude, longitude)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getOfflineData() {
        viewModelScope.launch {
            try {
                Repository.localDataExists(getApplication(), weatherInUserLocation)
            } catch (e: Exception) {
                println("ERROR: Cannot retrieve data from local db.")
            }
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    // Can be improved to check based on the season (Daylight is prone to change based on season)
    fun checkIfNight(): Boolean{
        val test = weatherInUserLocation.value?.current?.dt
        val sdf= java.text.SimpleDateFormat("HH")
        val date = Date((test?.times(1000L)!!))
        val hour = sdf.format(date).toInt()
        return hour>16 || hour<7
    }

    fun dateTimeToDate(day: Int): String{
        val sdf= java.text.SimpleDateFormat("dd/MM")
        val date = Date((weatherInUserLocation.value!!.daily[day].dt.times(1000L)!!))
        val formatedDate = sdf.format(date)
        return formatedDate
    }

    fun getCachedData(): MutableLiveData<Location>{
        return weatherInUserLocation
    }

    fun getCityName(lat: Double, lon: Double): String{
        if (weatherInUserLocation.value != null){
            var cityName: String?
            try{
                val geoCoder = Geocoder(getApplication(), Locale.getDefault())
                val address = geoCoder.getFromLocation(lat,lon,1)
                cityName = address[0].locality
                if (cityName == null){
                    cityName = address[0].adminArea
                    if (cityName == null){
                        cityName = address[0].subAdminArea
                    }
                }
                return cityName
            } catch (e: Exception){
                return "Unknown city"
            }
        }
        else{
            Toast.makeText(getApplication(),"Can't locate city, please use search", Toast.LENGTH_LONG).show()
            return "Unknown location"
        }
    }

    fun getSearchedLocation(cityName: String){
        try {
            val geoCoder = Geocoder(getApplication(), Locale.getDefault())
            val address = geoCoder.getFromLocationName(cityName, 1)
            requestWeatherData(address[0].latitude, address[0].longitude)
        }
        catch (e: Exception){

        }
    }

    fun notifyOnBadWeather(userAllowed: Boolean) {
        var isBadWeather = false
        if (userAllowed){
            if (weatherInUserLocation != null){
                when (weatherInUserLocation.value!!.hourly[0].weather[0].main){
                    "Rain","Thunderstorm","Tornado","Snow","Ash" -> isBadWeather=true
                }
                if (isBadWeather){
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                        .build()

                    val myRequest = PeriodicWorkRequest.Builder(
                        BadWeatherNotificationWorker::class.java,
                        30,
                        TimeUnit.MINUTES
                    ).setConstraints(constraints)
                        .addTag("notification_id")
                        .build()

                    WorkManager.getInstance(getApplication())
                        .enqueueUniquePeriodicWork(
                            "notification_id",
                            ExistingPeriodicWorkPolicy.KEEP,
                            myRequest
                        )
                }
                else{
                    WorkManager.getInstance(getApplication()).cancelAllWorkByTag("notification_id")
                }

            }
        }

    }
}


