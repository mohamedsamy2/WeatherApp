package com.example.weatherapp.features.favorites

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.core.data.local.room.entity.WeatherResponse
import com.example.weatherapp.core.repo.DBRepo

class FavoritesAdapter(
    private var favoritesList: ArrayList<WeatherResponse>, private val application: Application
):
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {
    private lateinit var recyclerView: RecyclerView

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.favIcon)
        val degree: TextView = view.findViewById(R.id.favDegree)
        val favName: TextView = view.findViewById(R.id.favName)
        val favDesc: TextView = view.findViewById(R.id.favDesc)
        val deleteBtn: ImageButton = view.findViewById(R.id.delete)

    }



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.favorites_row, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.favDesc.text =
            favoritesList[position].current.weather[0].description.capitalize()
        viewHolder.degree.text = "${favoritesList[position].current.temp.toInt()}°"
        viewHolder.favName.text =
            favoritesList[position].timezone.substringAfter("/").replace("_", " ").capitalize()
        Glide.with(viewHolder.itemView.context)
            .load("https://openweathermap.org/img/wn/${favoritesList[position].current.weather[0].icon}@2x.png")
            .into(viewHolder.icon)

        viewHolder.deleteBtn.setOnClickListener{
            DBRepo.getInstance(application).deleteWeather(favoritesList[position])
            favoritesList.remove(favoritesList[position])
            notifyDataSetChanged()
        }

    }

    fun setFavoritesList(favorites: ArrayList<WeatherResponse>) {
        this.favoritesList.clear()
        this.favoritesList.addAll(favorites)
        notifyDataSetChanged()
    }

    override fun getItemCount() = favoritesList.size


}