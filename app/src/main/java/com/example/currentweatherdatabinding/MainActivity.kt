package com.example.currentweatherdatabinding

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {

    // enum class DIRECTION {
    //     EAST, WEST, NORTH, NORTH_EAST, NORTH_WEST, SOUTH, SOUTH_EAST, SOUTH_WEST
    // }
    // enum class CLOUDS {
    //     SUN, RAIN, PARTLY, CLOUDY, SNOW, THUNDER
    // }
    val weather = ArrayList<WeatherData> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFromResources()

        val rv = findViewById<RecyclerView>(R.id.RecyclerView)
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        rv.layoutManager = layoutManager
        val adapter = WeatherAdapter(weather)
        rv.adapter = adapter

        val btn = findViewById<Button>(R.id.searchWeather)
        val searchField = findViewById<EditText>(R.id.searchField)
        btn.setOnClickListener {
            val city = searchField.text.toString()
            var ready = false
            GlobalScope.launch (Dispatchers.IO) {
                Log.d("MY_TAG", "Loading $city...")
                val weatherSingle = loadWeather(city)
                weatherSingle?.let {
                    for (item in weather) {
                        if (item.name == city) {
                            Log.d("MY_TAG", "$city already exists")
                            return@let
                        }
                    }
                    Log.d("MY_TAG", "City $city added")
                    weather.add(0, it)
                }
                ready = true
            }
            while (!ready) {
                // по какой-то причине без нормального тела цикл становится бесконечным
                if (ready) {
                    Log.d("MY_TAG", ready.toString())
                }
                continue
            }

            if (weather[0].name == city) {
                adapter.notifyItemInserted(0)
                layoutManager.scrollToPosition(0)
            }
            searchField.setText("")
        }
    }

    fun loadWeather(city: String): WeatherData? {
        val API_KEY = getString(R.string.api_key)
        val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$API_KEY&units=metric";
        return try {
            val stream = URL(weatherURL).getContent() as InputStream
            val jsonData = Scanner(stream).nextLine()
            val gson = Gson()
            gson.fromJson(jsonData, WeatherData::class.java)
        } catch (e: java.lang.Exception) {
            Log.e("MY_TAG", e.toString())
            null
        }
    }

    fun loadFromResources() {
        val cities = resources.getStringArray(R.array.cities)
        var cnt = 0 // считаю количество полученных городов, потому что
        // не умею ждать окончания треда :)

        for (city in cities) {
            GlobalScope.launch (Dispatchers.IO) {
                val weatherSingle = loadWeather(city)
                cnt++
                weatherSingle?.let {
                    weather.add(it)
                    Log.d("MY_TAG", "Loaded $city")
                }
            }
        }
        while (cnt < cities.size) {
            continue
        }
    }
}