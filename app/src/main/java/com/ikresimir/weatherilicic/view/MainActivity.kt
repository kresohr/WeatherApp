package com.ikresimir.weatherilicic.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.ikresimir.weatherilicic.R
import com.ikresimir.weatherilicic.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var btnShowForecast: Button
    lateinit var textMinValue: TextView
    lateinit var textMaxValue: TextView
    lateinit var textDegrees: TextView
    lateinit var textCity: TextView
    lateinit var textSkyState: TextView
    lateinit var imageWeather: ImageView
    lateinit var searchCity: SearchView
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //TO DO: Add string resources

        //Can be improved with viewBinding
        btnShowForecast = findViewById(R.id.btnShowForecast)
        textMinValue = findViewById(R.id.text_min_value)
        textMaxValue = findViewById(R.id.text_max_value)
        textCity = findViewById(R.id.text_city)
        textDegrees = findViewById(R.id.text_degrees)
        textSkyState = findViewById(R.id.text_sky_state)
        imageWeather = findViewById(R.id.image_weather)
        searchCity = findViewById(R.id.search)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getWeather().observe(this) {
            textDegrees.text = it.current.temp.toInt().toString() + " °C"
            textMinValue.text = it.daily[0].temp.min.toInt().toString() + " °C"
            textMaxValue.text = it.daily[0].temp.max.toInt().toString() + " °C"
            textSkyState.text = it.current.weather[0].main
            textCity.text = viewModel.getCityName()
            setCurrentWeatherImage(it.current.weather[0].main)
        }
    }

    private fun setCurrentWeatherImage(weatherDescription: String){
        val isNight = viewModel.checkIfNight()
        //TO DO: Add more weather modes "Fog, Sandstorm... etc"
        if(isNight){
            when(weatherDescription){
                "Thunderstorm" -> imageWeather.setImageResource(R.drawable.ic_thunder_night)
                "Rain" -> imageWeather.setImageResource(R.drawable.ic_rain_night)
                "Snow" -> imageWeather.setImageResource(R.drawable.ic_snow_night)
                "Clear" -> imageWeather.setImageResource(R.drawable.ic_clear_night)
                "Clouds" -> imageWeather.setImageResource(R.drawable.ic_cloud_night)
            }
        }
        else{
            when (weatherDescription){
                "Thunderstorm" -> imageWeather.setImageResource(R.drawable.ic_thunder)
                "Rain" -> imageWeather.setImageResource(R.drawable.ic_rain)
                "Snow" -> imageWeather.setImageResource(R.drawable.ic_snow)
                "Clear" -> imageWeather.setImageResource(R.drawable.ic_sun)
                "Clouds" -> imageWeather.setImageResource(R.drawable.ic_cloud)
            }
        }

    }

}