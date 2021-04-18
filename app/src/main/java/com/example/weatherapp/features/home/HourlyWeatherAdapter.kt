import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.core.data.model.Hourly
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

class HourlyWeatherAdapter() :
    RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder>() {
    private var hourlyWeather: List<Hourly> = ArrayList<Hourly>()

    fun setList(list: List<Hourly>){
        hourlyWeather = list
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val desc: TextView = view.findViewById(R.id.hourlyWeatherDesc)
        val temp: TextView = view.findViewById(R.id.hourlyWeatherTemp)
        val weatherImg: ImageView = view.findViewById(R.id.hourlyWeatherIcon)
        val hourlyTime: TextView = view.findViewById(R.id.hourlyTime)
    }



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.hourly_row, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.desc.text = hourlyWeather[position].weather[0].description.capitalize()
        viewHolder.temp.text = hourlyWeather[position].temp.toInt().toString() +"°"
        val instant = Instant.ofEpochSecond(hourlyWeather[position].dt.toLong());
        val date = Date.from(instant);
        viewHolder.hourlyTime.text = "${date.toString().split(" ")[3].dropLast(6)}:00"
        Glide.with(viewHolder.itemView.context)
            .load("https://openweathermap.org/img/wn/${hourlyWeather[position].weather[0].icon}@4x.png")
            .into(viewHolder.weatherImg)
    }

    override fun getItemCount() = hourlyWeather.size

}