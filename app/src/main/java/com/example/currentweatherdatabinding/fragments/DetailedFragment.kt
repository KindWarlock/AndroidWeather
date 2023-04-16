package com.example.currentweatherdatabinding.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currentweatherdatabinding.adapters.DetailedWeatherAdapter
import com.example.currentweatherdatabinding.R
import com.example.currentweatherdatabinding.adapters.WeatherDiffUtilCallback
import com.example.currentweatherdatabinding.ui.WeatherUIState
import com.example.currentweatherdatabinding.ui.WeatherViewModel
import kotlinx.coroutines.launch

class DetailedFragment(val weatherViewModel: WeatherViewModel) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MY_TAG", "Created ShortFragment")
        val view = inflater.inflate(R.layout.fragment_detailed, container, false)

        // RecyclerView
        val rv = view.findViewById<RecyclerView>(R.id.RecyclerView)
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rv.layoutManager = layoutManager
        val adapter = DetailedWeatherAdapter(WeatherUIState())
        rv.adapter = adapter

        // Default data
        val cities = resources.getStringArray(R.array.cities)
        for (city in cities) {
            weatherViewModel.addCity(city)
        }


        // Collecting data
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.uiState.collect { data ->
                    Log.d("MY_TAG", "New item! List is ${data.weatherList}!")
                    val diffUtilCallback = WeatherDiffUtilCallback(adapter.weather.weatherList, data.weatherList)
                    val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
                    adapter.update(data)
                    diffResult.dispatchUpdatesTo(adapter)
                }
            }
        }

        return view
    }
}