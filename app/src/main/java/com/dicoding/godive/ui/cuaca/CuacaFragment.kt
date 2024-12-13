package com.dicoding.godive.ui.cuaca

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dicoding.godive.R
import com.dicoding.godive.data.weather.response.WeatherResponse
import com.dicoding.godive.data.weather.retrofit.RetrofitClient
import com.dicoding.godive.data.weather.service.CityRequest
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CuacaFragment : Fragment(R.layout.fragment_cuaca) {

    private lateinit var editTextCity: TextInputEditText
    private lateinit var btnProcess: Button
    private lateinit var textViewCity: TextView
    private lateinit var textViewWeather: TextView
    private lateinit var textViewTemperature: TextView
    private lateinit var textViewHumidity: TextView
    private lateinit var textViewWindSpeed: TextView
    private lateinit var imageViewWeatherIcon: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        editTextCity = view.findViewById(R.id.editTextCity)
        btnProcess = view.findViewById(R.id.btnProcess)
        textViewCity = view.findViewById(R.id.textViewCity)
        textViewWeather = view.findViewById(R.id.textViewWeather)
        textViewTemperature = view.findViewById(R.id.textViewTemperature)
        textViewHumidity = view.findViewById(R.id.textViewHumidity)
        textViewWindSpeed = view.findViewById(R.id.textViewWindSpeed)
        imageViewWeatherIcon = view.findViewById(R.id.imageViewWeatherIcon)

        // Button click listener to get weather
        btnProcess.setOnClickListener {
            val city = editTextCity.text.toString().trim()
            if (city.isNotEmpty()) {
                getWeather(city)
            } else {
                Toast.makeText(requireContext(), "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getWeather(city: String) {
        val cityRequest = CityRequest(city)
        RetrofitClient.apiService.getWeather(cityRequest).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weather = response.body()
                    if (weather != null) {
                        updateUI(weather)
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(weather: WeatherResponse) {
        // Update UI with weather data
        textViewCity.text = "City: ${weather.city}"
        textViewWeather.text = "Weather: ${weather.weather}"
        textViewTemperature.text = "Temperature: ${weather.temperature}°C"
        textViewHumidity.text = "Humidity: ${weather.humidity}%"
        textViewWindSpeed.text = "Wind Speed: ${weather.windSpeed} m/s"

        // Set the weather icon dynamically based on the weather condition
        val weatherIcon = when (weather.weather.toLowerCase()) {
            "clear sky" -> R.drawable.ic_cloud // Ganti dengan icon cuaca sesuai kondisi
            "cloudy" -> R.drawable.ic_cloud
            "rainy" -> R.drawable.ic_cloud
            else -> R.drawable.ic_cloud
        }
        imageViewWeatherIcon.setImageResource(weatherIcon)
    }
}
