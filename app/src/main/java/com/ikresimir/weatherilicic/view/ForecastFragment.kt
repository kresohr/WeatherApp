package com.ikresimir.weatherilicic.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ikresimir.weatherilicic.R
import com.ikresimir.weatherilicic.databinding.FragmentForecastBinding

class ForecastFragment: Fragment(R.layout.fragment_forecast) {

    private var fragmentForecastBinding: FragmentForecastBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentForecastBinding.bind(view)
        fragmentForecastBinding = binding
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentForecastBinding = null
    }
}