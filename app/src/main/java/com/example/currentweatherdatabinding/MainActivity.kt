package com.example.currentweatherdatabinding

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currentweatherdatabinding.ui.WeatherUIState
import com.example.currentweatherdatabinding.ui.WeatherViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    // Creating variable for rv
    private var weather = WeatherUIState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ViewModel
        val weatherViewModel: WeatherViewModel by viewModels()

        // RecyclerView
        val rv = findViewById<RecyclerView>(R.id.RecyclerView)
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rv.layoutManager = layoutManager
        val adapter = WeatherAdapter(weather)
        rv.adapter = adapter

        // Default data
        val cities = resources.getStringArray(R.array.cities)
        for (city in cities) {
            weatherViewModel.addCity(city)
        }

        // Search
        val btn = findViewById<Button>(R.id.searchWeather)
        val searchField = findViewById<EditText>(R.id.searchField)
        btn.setOnClickListener {
            val city = searchField.text.toString()
            weatherViewModel.addCity(city)
            searchField.setText("")
        }



        // Collecting data
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.uiState.collect { data ->
//                    weather = data.copy()
                    adapter.update(data.copy())
                    adapter.notifyItemInserted(data.weatherList.size - 1)
                    Log.d("MY_TAG", "After: ${adapter.weather.weatherList.joinToString()}") // ничего нет????
                    //adapter.notifyDataSetChanged()
                    layoutManager.scrollToPosition(data.weatherList.size - 1)
                }
            }
        }
    }
}