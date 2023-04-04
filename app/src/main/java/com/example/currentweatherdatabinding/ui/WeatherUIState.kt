package com.example.currentweatherdatabinding.ui

import com.example.currentweatherdatabinding.data.WeatherData

data class WeatherUIState (
    val weatherList: ArrayList<WeatherData> = arrayListOf()
)