package com.example.currentweatherdatabinding

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currentweatherdatabinding.ui.WeatherUIState
import com.example.currentweatherdatabinding.ui.WeatherViewModel
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ViewModel
        val weatherViewModel: WeatherViewModel by viewModels()

        // RecyclerView
        val rv = findViewById<RecyclerView>(R.id.RecyclerView)
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rv.layoutManager = layoutManager
        val adapter = WeatherAdapter(WeatherUIState())
        rv.adapter = adapter

        // Default data
//        val cities = resources.getStringArray(R.array.cities)
//        for (city in cities) {
//            weatherViewModel.addCity(city)
//        }

        // Search
        val btn = findViewById<Button>(R.id.searchWeather)
        val searchField = findViewById<EditText>(R.id.searchField)
        btn.setOnClickListener {
            val city = searchField.text.toString()
            weatherViewModel.addCity(city)
            searchField.setText("")
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }


        // Collecting data
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.uiState.collect { data ->
                    Log.d("MY_TAG", "New item! List is ${data.weatherList}!")
                    val diffUtilCallback = WeatherDiffUtilCallback(adapter.weather.weatherList, data.weatherList)
                    val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
                    adapter.update(data)
                    diffResult.dispatchUpdatesTo(adapter)
                }
            }
        }
    }
}
