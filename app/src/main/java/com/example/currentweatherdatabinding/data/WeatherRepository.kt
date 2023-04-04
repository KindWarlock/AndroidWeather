package com.example.currentweatherdatabinding.data

import android.content.Context
import android.util.Log
import com.example.currentweatherdatabinding.R
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL
import java.util.*

class WeatherRepository(
    private val ioDispatcher: CoroutineDispatcher
) {
    private val apiKey = "4eccec95ad7702cb548cdf5f39f7b6c6"
    private var cities = mutableSetOf<String>()
    var weather = arrayListOf<WeatherData> ()

    private suspend fun fetchWeatherOne(city: String) {
        withContext(ioDispatcher) {
            val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric";
            try {
                val stream = URL(weatherURL).getContent() as InputStream
                val jsonData = Scanner(stream).nextLine()
                val gson = Gson()
                val response = gson.fromJson(jsonData, WeatherData::class.java)
                if (response.cod == 200) {
                    weather.add(gson.fromJson(jsonData, WeatherData::class.java))
                    Log.d("MY_TAG", "Fetched $city")
                } else {
                    Log.d("MY_TAG", "No city named $city. Removing.")
                    cities.remove(city)
                }
            } catch (e: java.lang.Exception) {
                Log.e("MY_TAG", e.toString())
            }
        }
    }

    suspend fun fetchWeatherAll() {
        for (city in cities) {
            fetchWeatherOne(city)
        }
    }

    suspend fun addCity(city: String) {
        cities.add(city)
        fetchWeatherOne(city)
    }
}