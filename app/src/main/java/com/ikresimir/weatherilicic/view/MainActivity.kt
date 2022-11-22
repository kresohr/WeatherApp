package com.ikresimir.weatherilicic.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.ikresimir.weatherilicic.R
import com.ikresimir.weatherilicic.databinding.ActivityMainBinding
import com.ikresimir.weatherilicic.utility.BadWeatherNotificationWorker
import com.ikresimir.weatherilicic.viewmodel.MainViewModel
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
    }


}