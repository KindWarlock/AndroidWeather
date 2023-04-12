package com.example.currentweatherdatabinding.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currentweatherdatabinding.data.WeatherData
import com.example.currentweatherdatabinding.data.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository(Dispatchers.IO)
    private var _uiState = MutableStateFlow(WeatherUIState())
    val uiState: StateFlow<WeatherUIState> = _uiState

    fun fetchWeather(city: String) {
        _uiState.update { it.copy(weatherList = repository.getWeatherAll()) }
        Log.d("MY_TAG", "Updated uiState with $city: ${repository.getWeatherAll()}")
    }

    fun addCity(city: String) {
        val cityTrim = city.filter { !it.isWhitespace() }
        viewModelScope.launch {
            if (repository.addCity(cityTrim)) {
                fetchWeather(cityTrim)
            }
        }
    }
}
