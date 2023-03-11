package com.example.currentweatherdatabinding

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class WeatherAdapter(val weather: ArrayList<WeatherData>) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        // Log.d("MY_TAG", "Created holder")
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.city.text = weather[position].name
        holder.tmp.text = weather[position].main.temp.toString()
        holder.humid.text = weather[position].main.humidity.toString()
        holder.windSpeed.text = weather[position].wind.speed.toString()
        holder.windDir.rotation = weather[position].wind.deg.toFloat()

        Picasso.get().load("http://openweathermap.org/img/wn/${weather[position].weather[0].icon}@2x.png").
        into(holder.icon, object: Callback {
            override fun onSuccess() {
                Log.d("MY_TAG", "Image downloaded")
            }

            override fun onError(e: java.lang.Exception?) {
                Log.e("MY_TAG", e.toString())
            }
        })
        // Log.d("MY_TAG", "Bind ${holder.city.text}")
    }

    override fun getItemCount(): Int {
        return weather.size
    }


}