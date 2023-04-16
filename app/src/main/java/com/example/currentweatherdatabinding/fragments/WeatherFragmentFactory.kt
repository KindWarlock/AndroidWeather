package com.example.currentweatherdatabinding.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.currentweatherdatabinding.ui.WeatherViewModel

class WeatherFragmentFactory(val weatherViewModel: WeatherViewModel, val isDetailed: Boolean) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        if (isDetailed) {
            when (loadFragmentClass(classLoader, className)) {
                DetailedFragment::class.java -> DetailedFragment(weatherViewModel)
                else -> super.instantiate(classLoader, className)
            }
        } else {
            when (loadFragmentClass(classLoader, className)) {
                ShortFragment::class.java -> ShortFragment(weatherViewModel)
                else -> super.instantiate(classLoader, className)
            }
        }

}