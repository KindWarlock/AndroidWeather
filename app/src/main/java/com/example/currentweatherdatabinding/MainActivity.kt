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
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {
    val weather = ArrayList<WeatherData> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rv = findViewById<RecyclerView>(R.id.RecyclerView)
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rv.layoutManager = layoutManager
        val adapter = WeatherAdapter(weather)
        rv.adapter = adapter
        GlobalScope.launch(Dispatchers.Main) {
            loadFromResources()
            Log.d("MY_TAG", "Data loaded")
            adapter.notifyDataSetChanged()
        }
        val btn = findViewById<Button>(R.id.searchWeather)
        val searchField = findViewById<EditText>(R.id.searchField)
        btn.setOnClickListener {
            val city = searchField.text.toString()
            GlobalScope.launch(Dispatchers.Main) {
                Log.d("MY_TAG", "Loading $city...")

                val weatherSingle = try {
                    loadWeather(city)
                } catch (e: java.lang.Exception) {
                    Log.e("MY_TAG", e.toString())
                    null
                }
                weatherSingle?.let {
                    for (item in weather) {
                        if (item.name == city) {
                            Log.d("MY_TAG", "$city already exists")
                            return@let
                        }
                    }
                    weather.add(0, it)
                    Log.d("MY_TAG", "City $city added")
                    adapter.notifyItemInserted(0)
                    layoutManager.scrollToPosition(0)
                }
            }
            searchField.setText("")
        }
    }

    suspend fun loadWeather(city: String): WeatherData? {
        return withContext(Dispatchers.IO) {
            val API_KEY = getString(R.string.api_key)
            val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$API_KEY&units=metric";
            val stream = URL(weatherURL).getContent() as InputStream
            val jsonData = Scanner(stream).nextLine()
            val gson = Gson()
            gson.fromJson(jsonData, WeatherData::class.java)
        }

    }

    suspend fun loadFromResources() {
        val cities = resources.getStringArray(R.array.cities)
            for (city in cities) {
                    val weatherSingle = try {
                        loadWeather(city)
                    } catch (e: java.lang.Exception) {
                        Log.e("MY_TAG", e.toString())
                        null
                    }
                    weatherSingle?.let {
                        weather.add(it)
                        Log.d("MY_TAG", "Loaded $city")
                    }
            }
    }
}