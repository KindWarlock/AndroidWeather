package com.example.currentweatherdatabinding

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.commit
import com.example.currentweatherdatabinding.fragments.DetailedFragment
import com.example.currentweatherdatabinding.fragments.ShortFragment
import com.example.currentweatherdatabinding.fragments.WeatherFragmentFactory
import com.example.currentweatherdatabinding.ui.MainViewModel
import com.example.currentweatherdatabinding.ui.WeatherViewModel

class MainActivity : AppCompatActivity(), DialogInterface.OnClickListener {
    val weatherViewModel: WeatherViewModel by viewModels()
    val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Fragment factory
        supportFragmentManager.fragmentFactory = WeatherFragmentFactory(weatherViewModel, mainViewModel.isDetailed)

        // Creating
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        title = getString(R.string.title)

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

        // AlertDialog
        val dlgChange = DesignAlertDialog()
        val args = Bundle()
        btnChange.setOnClickListener {
            args.putBoolean("isDetailed", mainViewModel.isDetailed)
            dlgChange.arguments = args
            dlgChange.show(supportFragmentManager, "ChangeDesign")
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            0 -> supportFragmentManager.commit {
                replace(R.id.fragment_container_view, ShortFragment(weatherViewModel))
                mainViewModel.isDetailed = false
            }
            1 -> supportFragmentManager.commit {
                replace(R.id.fragment_container_view, DetailedFragment(weatherViewModel))
                mainViewModel.isDetailed = true
            }
        }
        dialog?.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.lang, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val lang: String = when (item.title.toString()) {
            "Русский" -> "ru-RU"
            "English" -> "en-US"
            else -> "en-US"
        }
        val appLocale: LocaleListCompat =
            LocaleListCompat.forLanguageTags(lang)
        AppCompatDelegate.setApplicationLocales(appLocale)

        return true
    }
}
