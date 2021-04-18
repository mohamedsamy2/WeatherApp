package com.example.weatherapp.features.alarms

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AlarmsFragment : Fragment() {

    private lateinit var alarmsViewModel: AlarmsViewModel
    private lateinit var fab: FloatingActionButton
    private lateinit var alarmRecyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        alarmsViewModel =
            ViewModelProvider(this).get(AlarmsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_alarms, container, false)
        fab = root.findViewById(R.id.addAlarm)
        alarmRecyclerView = root.findViewById(R.id.alarmRecyclerView)
        fab.setOnClickListener{
            startActivity(Intent(this.context,AlarmActivity::class.java))
        }
        alarmRecyclerView.setHasFixedSize(true)
        val alarmRecyclerManager = LinearLayoutManager(root.context)
        alarmRecyclerManager.orientation = RecyclerView.VERTICAL
        alarmRecyclerView.layoutManager = alarmRecyclerManager


        return root
    }
}