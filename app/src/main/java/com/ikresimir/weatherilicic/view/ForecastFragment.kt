package com.ikresimir.weatherilicic.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ikresimir.weatherilicic.R
import com.ikresimir.weatherilicic.databinding.FragmentForecastBinding
import com.ikresimir.weatherilicic.viewmodel.MainViewModel

class ForecastFragment: Fragment(R.layout.fragment_forecast) {

    val viewModel: MainViewModel by activityViewModels()
    private var fragmentForecastBinding: FragmentForecastBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentForecastBinding.bind(view)
        fragmentForecastBinding = binding
        val weatherData = viewModel.getCachedData()

        binding.textForecastedCity.text = viewModel.getCityName(weatherData.value!!.lat, weatherData.value!!.lon )

        //Can be substituted with RecyclerView if more forecasted days are required.

        //Populate dates
        binding.textDate1.text = viewModel.dateTimeToDate(0)
        binding.textDate2.text = viewModel.dateTimeToDate(1)
        binding.textDate3.text = viewModel.dateTimeToDate(2)
        binding.textDate4.text = viewModel.dateTimeToDate(3)

        //Populate icons
        setCurrentWeatherImage(viewModel.getCachedData().value!!.daily[0].weather[0].main, binding.iconWeather1)
        setCurrentWeatherImage(viewModel.getCachedData().value!!.daily[1].weather[0].main, binding.iconWeather2)
        setCurrentWeatherImage(viewModel.getCachedData().value!!.daily[2].weather[0].main, binding.iconWeather3)
        setCurrentWeatherImage(viewModel.getCachedData().value!!.daily[3].weather[0].main, binding.iconWeather4)

        //Populate Min.temp
        binding.textMinTemperature1.text = viewModel.getCachedData().value!!.daily[0].temp.min.toInt().toString() + " °C"
        binding.textMinTemperature2.text = viewModel.getCachedData().value!!.daily[1].temp.min.toInt().toString() + " °C"
        binding.textMinTemperature3.text = viewModel.getCachedData().value!!.daily[2].temp.min.toInt().toString() + " °C"
        binding.textMinTemperature4.text = viewModel.getCachedData().value!!.daily[3].temp.min.toInt().toString() + " °C"

        //Populate Max.temp
        binding.textMaxTemperature1.text = viewModel.getCachedData().value!!.daily[0].temp.max.toInt().toString() + " °C"
        binding.textMaxTemperature2.text = viewModel.getCachedData().value!!.daily[1].temp.max.toInt().toString() + " °C"
        binding.textMaxTemperature3.text = viewModel.getCachedData().value!!.daily[2].temp.max.toInt().toString() + " °C"
        binding.textMaxTemperature4.text = viewModel.getCachedData().value!!.daily[3].temp.max.toInt().toString() + " °C"


    }

    private fun setCurrentWeatherImage(weatherDescription: String, imageView: ImageView) {
        when (weatherDescription){
            "Thunderstorm" -> imageView.setImageResource(R.drawable.ic_thunder)
            "Rain" -> imageView.setImageResource(R.drawable.ic_rain)
            "Drizzle" -> imageView.setImageResource(R.drawable.ic_rain)
            "Fog" -> imageView.setImageResource(R.drawable.ic_fog)
            "Mist" -> imageView.setImageResource(R.drawable.ic_fog)
            "Sandstorm" -> imageView.setImageResource(R.drawable.ic_sandstorm)
            "Snow" -> imageView.setImageResource(R.drawable.ic_snow)
            "Clear" -> imageView.setImageResource(R.drawable.ic_sun)
            "Clouds" -> imageView.setImageResource(R.drawable.ic_cloud)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentForecastBinding = null
    }
}