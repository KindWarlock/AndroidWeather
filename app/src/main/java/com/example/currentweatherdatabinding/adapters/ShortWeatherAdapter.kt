package com.example.currentweatherdatabinding.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currentweatherdatabinding.R
import com.example.currentweatherdatabinding.ui.WeatherUIState
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ShortWeatherAdapter(
    var weather: WeatherUIState
) :
    RecyclerView.Adapter<ShortWeatherAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var city: TextView
        var tmp: TextView
        var icon: ImageView

        init {
            city = itemView.findViewById(R.id.city)
            tmp = itemView.findViewById(R.id.tmp)
            icon = itemView.findViewById(R.id.icon)
        }

    }

    fun update(w: WeatherUIState)  {
        weather = w
        Log.d("MY_TAG", "Data updated")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_short, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.city.text = weather.weatherList[position].name
        holder.tmp.text = weather.weatherList[position].main.temp.toString()

        Picasso.get().load("http://openweathermap.org/img/wn/${weather.weatherList[position].weather[0].icon}@2x.png").
        into(holder.icon, object: Callback {
            override fun onSuccess() {
                return
            }

            override fun onError(e: java.lang.Exception?) {
                Log.e("MY_TAG", e.toString())
            }
        })
        Log.d("MY_TAG", "Bind ${holder.city.text}")
    }

    override fun getItemCount(): Int {
        return weather.weatherList.size
    }
}