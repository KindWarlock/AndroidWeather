package com.example.currentweatherdatabinding

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
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
        // ViewModel
        val weatherViewModel: WeatherViewModel by viewModels()

        // Fragments
        var isDetailed = false
        supportFragmentManager.fragmentFactory = WeatherFragmentFactory(weatherViewModel, false)

        // Creating
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        // Changing fragments
        val btnChange = findViewById<Button>(R.id.changeFragment)
        btnChange.setOnClickListener {
            if (isDetailed) {
                supportFragmentManager.commit {
                    replace(R.id.fragment_container_view, ShortFragment(weatherViewModel))
                }
            } else {
                supportFragmentManager.commit {
                    replace(R.id.fragment_container_view, DetailedFragment(weatherViewModel))
                }
            }
            isDetailed = !isDetailed
        }
    }
}
