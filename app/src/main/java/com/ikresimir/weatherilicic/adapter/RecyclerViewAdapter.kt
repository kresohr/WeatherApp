package com.ikresimir.weatherilicic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikresimir.weatherilicic.R
import com.ikresimir.weatherilicic.model.Hourly
import java.util.*

class RecyclerViewAdapter() : RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {

    val hourlyWeatherList = mutableListOf<Hourly>()

    fun addHourlyList(hourlyList: List<Hourly>){
        hourlyWeatherList.clear()
        hourlyWeatherList.addAll(hourlyList)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtItemTemperature: TextView = itemView.findViewById(R.id.item_text_temperature)
        val txtItemHour: TextView = itemView.findViewById(R.id.item_text_hour)
        val imgItemWeather: ImageView = itemView.findViewById(R.id.item_icon)
        val txtItemDate: TextView = itemView.findViewById(R.id.item_text_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hourly_weather, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentHour = hourlyWeatherList[position]

        setCurrentWeatherImage(currentHour.weather[0].main,currentHour.dt,holder.imgItemWeather)
        holder.txtItemHour.text = dateTimeToHour(currentHour.dt)
        holder.txtItemDate.text = dateTimeToDate(currentHour.dt)
        holder.txtItemTemperature.text = currentHour.temp.toInt().toString()+"°C"
    }

    override fun getItemCount(): Int {
        return hourlyWeatherList.size
    }

    private fun dateTimeToHour(hour: Int): String{
        val sdf= java.text.SimpleDateFormat("HH:mm")
        val date = Date((hour.times(1000L)))
        val formatedDate = sdf.format(date)
        return formatedDate
    }

    private fun dateTimeToDate(hour: Int): String{
        val sdf= java.text.SimpleDateFormat("dd/MM")
        val date = Date((hour.times(1000L)))
        val formatedDate = sdf.format(date)
        return formatedDate
    }

    private fun setCurrentWeatherImage(weatherDescription: String, dateTime: Int, weatherIcon: ImageView){
        val isNight = checkIfNight(dateTime)
        if(isNight){
            when(weatherDescription){
                "Thunderstorm" -> weatherIcon.setImageResource(R.drawable.ic_thunder_night)
                "Rain" -> weatherIcon.setImageResource(R.drawable.ic_rain_night)
                "Drizzle" -> weatherIcon.setImageResource(R.drawable.ic_rain_night)
                "Sandstorm" -> weatherIcon.setImageResource(R.drawable.ic_sandstorm)
                "Snow" -> weatherIcon.setImageResource(R.drawable.ic_snow_night)
                "Fog" -> weatherIcon.setImageResource(R.drawable.ic_fog)
                "Mist" -> weatherIcon.setImageResource(R.drawable.ic_fog)
                "Clear" -> weatherIcon.setImageResource(R.drawable.ic_clear_night)
                "Clouds" -> weatherIcon.setImageResource(R.drawable.ic_cloud_night)
            }
        }
        else{
            when (weatherDescription){
                "Thunderstorm" -> weatherIcon.setImageResource(R.drawable.ic_thunder)
                "Rain" -> weatherIcon.setImageResource(R.drawable.ic_rain)
                "Drizzle" -> weatherIcon.setImageResource(R.drawable.ic_rain)
                "Fog" -> weatherIcon.setImageResource(R.drawable.ic_fog)
                "Mist" -> weatherIcon.setImageResource(R.drawable.ic_fog)
                "Sandstorm" -> weatherIcon.setImageResource(R.drawable.ic_sandstorm)
                "Snow" -> weatherIcon.setImageResource(R.drawable.ic_snow)
                "Clear" -> weatherIcon.setImageResource(R.drawable.ic_sun)
                "Clouds" -> weatherIcon.setImageResource(R.drawable.ic_cloud)
            }
        }
    }

   private fun checkIfNight(dateTime: Int): Boolean{
        val sdf= java.text.SimpleDateFormat("HH")
        val date = Date((dateTime.times(1000L)!!))
        val hour = sdf.format(date).toInt()
        return hour>16 || hour<7
    }
}