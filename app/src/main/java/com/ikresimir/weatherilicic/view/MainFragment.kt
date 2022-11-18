package com.ikresimir.weatherilicic.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ikresimir.weatherilicic.R
import com.ikresimir.weatherilicic.databinding.FragmentMainBinding
import com.ikresimir.weatherilicic.viewmodel.MainViewModel

class MainFragment: Fragment(R.layout.fragment_main) {

    lateinit var viewModel: MainViewModel
    private var fragmentMainBinding: FragmentMainBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMainBinding.bind(view)
        fragmentMainBinding = binding

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getWeather().observe(viewLifecycleOwner) {
            binding.textDegrees.text = it.current.temp.toInt().toString() + " °C"
            binding.textMinValue.text = it.daily[0].temp.min.toInt().toString() + " °C"
            binding.textMaxValue.text = it.daily[0].temp.max.toInt().toString() + " °C"
            binding.textSkyState.text = it.current.weather[0].main
            binding.textCity.text = viewModel.getCityName()
            setCurrentWeatherImage(it.current.weather[0].main)
            binding.btnShowForecast.setOnClickListener {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToForecastFragment())
            }
        }
    }

    private fun setCurrentWeatherImage(weatherDescription: String){
        val isNight = viewModel.checkIfNight()
        //TO DO: Add more weather modes "Fog, Sandstorm... etc"
        if(isNight){
            when(weatherDescription){
                "Thunderstorm" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_thunder_night)
                "Rain" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_rain_night)
                "Snow" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_snow_night)
                "Clear" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_clear_night)
                "Clouds" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_cloud_night)
            }
        }
        else{
            when (weatherDescription){
                "Thunderstorm" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_thunder)
                "Rain" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_rain)
                "Snow" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_snow)
                "Clear" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_sun)
                "Clouds" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_cloud)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentMainBinding = null
    }
}