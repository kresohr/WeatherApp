package com.ikresimir.weatherilicic.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ikresimir.weatherilicic.R
import com.ikresimir.weatherilicic.databinding.ActivityMainBinding

import com.ikresimir.weatherilicic.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getWeather().observe(this) {
            binding.textDegrees.text = it.current.temp.toInt().toString() + " °C"
            binding.textMinValue.text = it.daily[0].temp.min.toInt().toString() + " °C"
            binding.textMaxValue.text = it.daily[0].temp.max.toInt().toString() + " °C"
            binding.textSkyState.text = it.current.weather[0].main
            binding.textCity.text = viewModel.getCityName()
            setCurrentWeatherImage(it.current.weather[0].main)
        }
    }

    private fun setCurrentWeatherImage(weatherDescription: String){
        val isNight = viewModel.checkIfNight()
        //TO DO: Add more weather modes "Fog, Sandstorm... etc"
        if(isNight){
            when(weatherDescription){
                "Thunderstorm" -> binding.imageWeather.setImageResource(R.drawable.ic_thunder_night)
                "Rain" -> binding.imageWeather.setImageResource(R.drawable.ic_rain_night)
                "Snow" -> binding.imageWeather.setImageResource(R.drawable.ic_snow_night)
                "Clear" -> binding.imageWeather.setImageResource(R.drawable.ic_clear_night)
                "Clouds" -> binding.imageWeather.setImageResource(R.drawable.ic_cloud_night)
            }
        }
        else{
            when (weatherDescription){
                "Thunderstorm" -> binding.imageWeather.setImageResource(R.drawable.ic_thunder)
                "Rain" -> binding.imageWeather.setImageResource(R.drawable.ic_rain)
                "Snow" -> binding.imageWeather.setImageResource(R.drawable.ic_snow)
                "Clear" -> binding.imageWeather.setImageResource(R.drawable.ic_sun)
                "Clouds" -> binding.imageWeather.setImageResource(R.drawable.ic_cloud)
            }
        }

    }

}