package com.example.weatherapp.features.favorites

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.core.repo.DBRepo
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class FavoriteActivity : AppCompatActivity() {
    lateinit var enteredLocation:EditText
    lateinit var saveBtn:Button
    lateinit var cancelBtn: Button
    lateinit var place: Place
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val favoritesViewModel =
            ViewModelProvider(
                this,
                FavoritesViewModelFactory(application)
            ).get(FavoritesViewModel::class.java)

        enteredLocation = findViewById(R.id.enteredLocation)
        saveBtn = findViewById(R.id.save)
        cancelBtn = findViewById(R.id.cancel)
        enteredLocation.focusable = View.NOT_FOCUSABLE;
        Places.initialize(
            applicationContext,
            "AIzaSyCAyrJNax_LnPoGuH_-uFItTXm1stGo0Xg"
        )

        enteredLocation.setOnClickListener {
            var fieldList = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
            startActivityForResult(
                Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN,
                    fieldList
                ).build(this), 1
            )
            true
        }

        saveBtn.setOnClickListener {
              favoritesViewModel.insertLocationIntoDB(place)
                DBRepo.getInstance(application).getFavorites()


                finish()


        }
        cancelBtn.setOnClickListener{finish()}
    }



    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            place = Autocomplete.getPlaceFromIntent(data!!)
            enteredLocation.text = Editable.Factory.getInstance().newEditable(place.address)

        } else {
            val status =
                Autocomplete.getStatusFromIntent(data!!)
            Toast.makeText(
                applicationContext,
                status.statusMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}