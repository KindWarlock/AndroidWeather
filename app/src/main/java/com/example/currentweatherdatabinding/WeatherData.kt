package com.example.currentweatherdatabinding

data class WeatherData (
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val name: String)

data class Weather (
    val id: Int,
    val main: String,
    val description: String,
    val icon: String)

data class Main (
    val temp: Float,
    val pressure: Int,
    val humidity: Int)

data class Wind (
    val speed: Float,
    val deg: Int)