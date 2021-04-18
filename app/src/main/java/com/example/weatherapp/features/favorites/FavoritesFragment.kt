package com.example.weatherapp.features.favorites

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var fab: FloatingActionButton;
    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter
    lateinit var myApp: Application

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myApp = requireActivity().application

        favoritesViewModel =
            ViewModelProvider(
                this,
                FavoritesViewModelFactory(requireActivity().application)
            ).get(FavoritesViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_favorites, container, false)

        favoritesViewModel.updateFavorites()

        fab = root.findViewById(R.id.addFavorite)
        favoritesRecyclerView = root.findViewById(
            R.id.favoritesRecyclerView
        )
        favoritesRecyclerView.setHasFixedSize(true)
        val favoritesRecyclerManager = LinearLayoutManager(root.context)
        favoritesRecyclerManager.orientation = RecyclerView.VERTICAL
        favoritesRecyclerView.layoutManager = favoritesRecyclerManager


        fab.setOnClickListener {
            startActivity(
                Intent(this.context, FavoriteActivity::class.java)
            )
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesViewModel.favoritesListLiveData.observe(
            viewLifecycleOwner,
            Observer {
                favoritesAdapter = FavoritesAdapter(it, requireActivity().application)
                favoritesRecyclerView.adapter = favoritesAdapter
            })



    }



}