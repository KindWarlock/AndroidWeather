package com.example.currentweatherdatabinding

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currentweatherdatabinding.data.WeatherData
import com.example.currentweatherdatabinding.ui.WeatherUIState
import com.example.currentweatherdatabinding.ui.WeatherViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.StateFlow

class WeatherAdapter(
    var weather: WeatherUIState
) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var city: TextView
        var tmp: TextView
        var humid: TextView
        var icon: ImageView
        var windSpeed: TextView
        var windDir: ImageView

        init {
            city = itemView.findViewById(R.id.city)
            tmp = itemView.findViewById(R.id.tmp)
            humid = itemView.findViewById(R.id.humid)
            icon = itemView.findViewById(R.id.icon)
            windSpeed = itemView.findViewById(R.id.wind)
            windDir = itemView.findViewById(R.id.windIcon)
        }

    }

    fun update(w: WeatherUIState)  {
        weather = w
        Log.d("MY_TAG", "Data updated")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.city.text = weather.weatherList[position].name
        holder.tmp.text = weather.weatherList[position].main.temp.toString()
        holder.humid.text = weather.weatherList[position].main.humidity.toString()
        holder.windSpeed.text = weather.weatherList[position].wind.speed.toString()
        holder.windDir.rotation = weather.weatherList[position].wind.deg.toFloat()

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

class WeatherDiffUtilCallback(private val oldList: ArrayList<WeatherData>,
                            private val newList:ArrayList<WeatherData>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].weather == newList[newItemPosition].weather &&
                oldList[oldItemPosition].main == newList[newItemPosition].main &&
                oldList[oldItemPosition].wind == newList[newItemPosition].wind
    }
}