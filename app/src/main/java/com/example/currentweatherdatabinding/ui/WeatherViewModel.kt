package com.example.currentweatherdatabinding.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currentweatherdatabinding.data.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository(Dispatchers.IO)
    private val _uiState = MutableStateFlow(WeatherUIState())
    val uiState: StateFlow<WeatherUIState> = _uiState.asStateFlow()

    fun fetchWeather(city: String) {
        _uiState.update { it.copy(weatherList = repository.weather) }
        Log.d("MY_TAG", "Updated uiState with $city")
    }

    fun addCity(city: String) {
        viewModelScope.launch {
            repository.addCity(city)
            fetchWeather(city)
        }
    }
}
