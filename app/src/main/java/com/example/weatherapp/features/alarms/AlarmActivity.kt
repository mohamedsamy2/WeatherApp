package com.example.weatherapp.features.alarms

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.core.DatePickerFragment
import com.example.weatherapp.R
import com.example.weatherapp.core.TimePickerFragment
import com.example.weatherapp.alarms.Alarm.setAlarm
import com.example.weatherapp.databinding.ActivityAlarmBinding
import java.util.*


class AlarmActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: ActivityAlarmBinding
    private lateinit var calendarBtn: ImageButton
    private lateinit var dateText: TextView
    private lateinit var timeText: TextView
    private lateinit var clockBtn: ImageButton
    private lateinit var saveBtn: Button
    private lateinit var cancelBtn: Button
    val calendar: Calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        calendarBtn = findViewById(R.id.calendarBtn)
        dateText = findViewById(R.id.dateText)
        timeText = findViewById(R.id.timeText)
        clockBtn = findViewById(R.id.clockBtn)
        cancelBtn = findViewById(R.id.cancelAlarm)
        saveBtn = findViewById(R.id.saveAlarm)
        calendarBtn.setOnClickListener {
            Log.d("TAG", "onCreate: PRESSeD")
            DatePickerFragment(this).show(supportFragmentManager, "DatePicker")
            //            datePicker.show(supportFragmentManager, "DatePicker")
        }

        clockBtn.setOnClickListener {
            Log.d("TAG", "onCreate: PRESSeD")
            TimePickerFragment(this).show(supportFragmentManager, "TimePicker")
        }

        cancelBtn.setOnClickListener { finish() }

        saveBtn.setOnClickListener {
            setAlarm(this, calendar.timeInMillis)
            finish()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dateText.text = "" + year + "/" + month + 1 + "/" + dayOfMonth
        calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        timeText.text = "" + hourOfDay + ":" + minute
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
    }


}