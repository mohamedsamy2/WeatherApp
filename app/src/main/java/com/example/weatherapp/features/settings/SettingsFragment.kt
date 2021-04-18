package com.example.weatherapp.features.settings

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.preference.*
import com.example.weatherapp.R
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var customLocation: Preference
    private lateinit var currentLocationCheck: SwitchPreference
    private lateinit var language: ListPreference
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        customLocation = findPreference<EditTextPreference>("PROVIDED_LOCATION")!!
        Places.initialize(
            requireActivity().applicationContext,
            "AIzaSyCAyrJNax_LnPoGuH_-uFItTXm1stGo0Xg"
        )
        language = findPreference<ListPreference>("LANGUAGE")!!
        customLocation?.setOnPreferenceClickListener {
            var fieldList = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
            startActivityForResult(
                Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN,
                    fieldList
                ).build(requireActivity()), 1
            )
            true
        }

        language.setOnPreferenceChangeListener { preference, newValue ->
                val resources: Resources = requireContext().getResources()
                val locale = Locale(newValue as String)
                Locale.setDefault(locale);
                val config = Configuration()
                config.locale = locale
                resources.updateConfiguration(config, resources.getDisplayMetrics());
                activity?.finish()
                activity?.startActivity(activity?.getIntent());

            true
        }

        view?.setBackgroundColor(Color.parseColor("#2F406E"))


    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data!!)

            customLocation.summary = place.name
            customLocation.sharedPreferences.edit().putString(
                customLocation.key,
                "${place.latLng?.latitude}/${place.latLng?.longitude}"
            )
                .apply()
        } else {
            val status =
                Autocomplete.getStatusFromIntent(data!!)
            Toast.makeText(
                requireActivity().applicationContext,
                "status.statusMessage",
                Toast.LENGTH_LONG
            ).show()
        }
    }


}