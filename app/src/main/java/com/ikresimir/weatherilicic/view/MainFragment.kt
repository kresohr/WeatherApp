package com.ikresimir.weatherilicic.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.LOCATION_SERVICE
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationRequest.PRIORITY_LOW_POWER
import com.google.android.gms.location.LocationServices
import com.ikresimir.weatherilicic.R
import com.ikresimir.weatherilicic.adapter.RecyclerViewAdapter
import com.ikresimir.weatherilicic.databinding.FragmentMainBinding
import com.ikresimir.weatherilicic.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainFragment : Fragment(R.layout.fragment_main) {

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                dialog.dismiss()
                checkLocationPermission()
            } else {
                dialog.dismiss()
            }
        }

    lateinit private var dialog: Dialog
    private val viewModel: MainViewModel by activityViewModels()
    private var fragmentMainBinding: FragmentMainBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMainBinding.bind(view)
        fragmentMainBinding = binding

        checkLocationPermission()
        binding.btnShowForecast.visibility = View.INVISIBLE
        val adapter = RecyclerViewAdapter()
        fragmentMainBinding!!.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        hideViews()

        // Observe LiveData and populate UI with its data
        viewModel.getWeather().observe(viewLifecycleOwner) {
            binding.textDegrees.text = it.current.temp.toInt().toString() + " °C"
            binding.textMinValue.text = it.daily[0].temp.min.toInt().toString() + " °C"
            binding.textMaxValue.text = it.daily[0].temp.max.toInt().toString() + " °C"
            binding.textSkyState.text = it.current.weather[0].main
            binding.btnShowForecast.isEnabled = true
            binding.textCity.text = viewModel.getCityName(it.lat, it.lon)
            setCurrentWeatherImage(it.current.weather[0].main)
            adapter.addHourlyList(it.hourly)
            hideViews()
            showViews()
            binding.btnShowForecast.visibility = View.VISIBLE
            binding.btnShowForecast.setOnClickListener {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToForecastFragment())
            }
            binding.btnNotification.visibility = View.VISIBLE
        }

        // Handler for notification icon
        binding.btnNotification.setOnClickListener {
            dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_notification)
            dialog.show()
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val btnSure: Button = dialog.findViewById(R.id.btnSure)
            val btnNoThanks: Button = dialog.findViewById(R.id.btnNoThanks)

            btnSure.setOnClickListener {
                binding.btnNotification.setImageResource(R.drawable.ic_notification_active)
                viewModel.notifyOnBadWeather(true)
                dialog.dismiss()
            }

            btnNoThanks.setOnClickListener {
                binding.btnNotification.setImageResource(R.drawable.ic_notification)
                viewModel.notifyOnBadWeather(false)
                dialog.dismiss()
            }
        }

        // Handler for search input
        binding.searchInput.setOnEditorActionListener { textView, i, keyEvent ->
            val inputMethodManager =
                requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            if (viewModel.isOnline(requireActivity())) {
                viewModel.getSearchedLocation(textView.text.toString())
                hideViews()
                binding.lottieAnimation.setAnimation(R.raw.anim_searching)
                binding.lottieAnimation.playAnimation()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Check your internet connection.",
                    Toast.LENGTH_LONG
                ).show()
            }
            true
        }
    }

    private fun checkLocationPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                val locationManager =
                    requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
                val isLocationEnabled =
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isInternetEnabled = viewModel.isOnline(requireActivity())

                // If user has enabled location settings, fetch data
                if (isLocationEnabled && isInternetEnabled) {
                    lifecycleScope.launch {
                        val fusedLocationClient =
                            LocationServices.getFusedLocationProviderClient(requireActivity())
                        delay(1000L)
                        fusedLocationClient.getCurrentLocation(PRIORITY_LOW_POWER, null)
                            .addOnSuccessListener(requireActivity()) { location ->
                                if (location != null) {
                                    println("Location: ${location.latitude}")
                                    viewModel.requestWeatherData(
                                        location.latitude,
                                        location.longitude
                                    )
                                }
                            }
                    }
                } else {
                    if (!viewModel.permissionDialogShown) {
                        dialog = Dialog(requireContext())
                        dialog.setContentView(R.layout.dialog_error_fetching)
                        dialog.show()
                        dialog.window?.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        val btnOkay: Button = dialog.findViewById(R.id.btnOkay)
                        btnOkay.setOnClickListener {
                            dialog.dismiss()
                            hideViews()
                            viewModel.getOfflineData()
                            viewModel.permissionDialogShown = true
                        }
                    }


                }
            }

            // If the user didn't allow Location permission yet, show dialog and ask for it
            else -> {
                if (!viewModel.permissionDialogShown) {
                    dialog = Dialog(requireContext())
                    dialog.setContentView(R.layout.dialog_location)
                    dialog.show()
                    dialog.window?.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    val btnNoThanks: Button = dialog.findViewById(R.id.btnNoThanks)
                    val btnSure: Button = dialog.findViewById(R.id.btnSure)

                    btnNoThanks.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.setOnDismissListener {
                        hideViews()
                        viewModel.getOfflineData()
                        viewModel.permissionDialogShown = true
                    }
                    btnSure.setOnClickListener {
                        requestLocationPermission()
                    }
                }

            }
        }
    }

    private fun setCurrentWeatherImage(weatherDescription: String) {
        val isNight = viewModel.checkIfNight()
        if (isNight) {
            when (weatherDescription) {
                "Thunderstorm" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_thunder_night)
                "Rain" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_rain_night)
                "Drizzle" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_rain_night)
                "Sandstorm" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_sandstorm)
                "Snow" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_snow_night)
                "Fog" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_fog)
                "Mist" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_fog)
                "Clear" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_clear_night)
                "Clouds" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_cloud_night)
            }
        } else {
            when (weatherDescription) {
                "Thunderstorm" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_thunder)
                "Rain" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_rain)
                "Drizzle" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_rain)
                "Fog" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_fog)
                "Mist" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_fog)
                "Sandstorm" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_sandstorm)
                "Snow" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_snow)
                "Clear" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_sun)
                "Clouds" -> fragmentMainBinding!!.imageWeather.setImageResource(R.drawable.ic_cloud)
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun requestLocationPermission() {

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                Toast.makeText(requireContext(), "PERMISSION GRANTED", Toast.LENGTH_LONG).show()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }

    }

    private fun hideViews() {
        val layout = fragmentMainBinding!!.mainFragmentLayout
        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)
            child.isVisible = false
        }
        fragmentMainBinding!!.search.isVisible = true
        fragmentMainBinding!!.textOfflinePlaceholder.isVisible = true
        fragmentMainBinding!!.lottieAnimation.visibility = View.VISIBLE
    }

    private fun showViews() {
        val layout = fragmentMainBinding!!.mainFragmentLayout
        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)
            child.isVisible = true
        }
        fragmentMainBinding!!.textAnimationDescription.visibility = View.GONE
        fragmentMainBinding!!.lottieAnimation.pauseAnimation()
        fragmentMainBinding!!.lottieAnimation.visibility = View.GONE
        fragmentMainBinding!!.textOfflinePlaceholder.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentMainBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requestPermissionLauncher.unregister()
    }

}