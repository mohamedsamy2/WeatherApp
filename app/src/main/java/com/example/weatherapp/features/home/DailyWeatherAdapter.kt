import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.core.data.model.Daily
import java.time.Instant
import java.util.*

class DailyWeatherAdapter(private val dailyWeather: ArrayList<Daily>) :
    RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val desc: TextView = view.findViewById(R.id.dailyDesc)
        val degree: TextView = view.findViewById(R.id.dailyDegree)
        val icon: ImageView = view.findViewById(R.id.dailyIcon)
        val day: TextView = view.findViewById(R.id.dailyDay)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.daily_row, viewGroup, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.desc.text = dailyWeather[position].weather[0].description
        viewHolder.degree.text =
            "${dailyWeather[position].temp.max.toInt()}°/${dailyWeather[position].temp.min.toInt()}°"
        Glide.with(viewHolder.itemView.context)
            .load("https://openweathermap.org/img/wn/${dailyWeather[position].weather[0].icon}@2x.png")
            .into(viewHolder.icon)
        val instant = Instant.ofEpochSecond(dailyWeather[position].dt.toLong());
        val date = Date.from(instant);
        viewHolder.day.text = date.toString().split(" ")[0]

    }

    fun setList(arr: ArrayList<Daily>){
        dailyWeather.clear()
        dailyWeather.addAll(arr)
        notifyDataSetChanged()
    }

    override fun getItemCount() = dailyWeather.size

}