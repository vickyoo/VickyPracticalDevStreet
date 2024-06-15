package com.devstreeit.vickypracticaldevstreeit.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.devstreeit.vickypracticaldevstreeit.R
import com.devstreeit.vickypracticaldevstreeit.database.model.PlaceEntity
import com.devstreeit.vickypracticaldevstreeit.database.repo.LocationRepository
import com.devstreeit.vickypracticaldevstreeit.databinding.ActivityMapBinding
import com.devstreeit.vickypracticaldevstreeit.viewmodel.LocationViewModel
import com.devstreeit.vickypracticaldevstreeit.utility.locationDataKey
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MapScreen : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding
    private lateinit var mMap: GoogleMap
    private var latlng = LatLng(0.0, 0.0)
    private var placeName: String = ""
    private var placeAddress: String = ""
    private val locationViewModel: LocationViewModel by viewModels()
    private var placeEntity: PlaceEntity? = null
    private var isLocationModified = false
    @Inject
    lateinit var locationRepository : LocationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUi()
    }

    private fun setUpUi() {
        try {
            val apiKey = resources.getString(R.string.google_map_key)
            Places.initialize(this@MapScreen, apiKey)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        initMap()
        onClick()
        setToolBar()

        placeEntity = intent.getParcelableExtra(locationDataKey)
        if (placeEntity != null) {
            binding.layoutDialog.root.isVisible = true
            binding.layoutDialog.txtSave.text = getString(R.string.label_update)
        }
    }

    private fun setToolBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Search Places"
    }

    private fun onClick() {
        binding.etAddress.setOnClickListener {
            searchAddressIntent()
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.layoutDialog.txtSave.setOnClickListener {
            binding.layoutDialog.root.isGone = true

            lifecycleScope.launch {
                val placeList = withContext(Dispatchers.IO) {
                    locationRepository.checkList()
                }

                if (placeEntity != null) {
                    placeEntity!!.name = placeName
                    placeEntity!!.address = placeAddress
                    placeEntity!!.latitude = latlng.latitude
                    placeEntity!!.longitude = latlng.longitude
                    placeEntity!!.primary = false

                    if (isLocationModified) {
                        locationViewModel.updateLocation(placeEntity!!)
                    }
                } else {
                    val data = PlaceEntity(
                        name = placeName,
                        address = placeAddress,
                        latitude = latlng.latitude,
                        longitude = latlng.longitude,
                        primary = placeList.isEmpty()
                    )
                    locationViewModel.insertLocation(data)
                }

                val intent = Intent(this@MapScreen, MapListScreen::class.java)
                startActivity(intent)
            }
        }

    }

    private fun initMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun searchAddressIntent() {
        val fields: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS
        )

        val intent: Intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        ).setCountry("")
            .build(this@MapScreen)
        addressPickerLauncher.launch(
            intent
        )
    }

    private val addressPickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.data == null)
                return@registerForActivityResult
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(result.data!!)
                    Log.e("TAG", "Place: " + place.name + ", " + place.id)

                    if (place.latLng != null) {
                        latlng = place.latLng!!
                        placeName = place.name!!
                        placeAddress = place.address!!
                        addMarker(latlng, place.name!!)
                        isLocationModified = true
                    }
                    binding.etAddress.setText(place.address!!.toString())
                }

                AutocompleteActivity.RESULT_ERROR -> {
                    val status = Autocomplete.getStatusFromIntent(result.data!!)
                    Log.e("TAG", "${status.statusMessage}")
                }

                Activity.RESULT_CANCELED -> {
                    binding.etAddress.setText("")
                }
            }
        }

    private fun addMarker(latLng: LatLng, title: String) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(latLng).title(title))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        binding.layoutDialog.root.isVisible = true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (placeEntity != null) {
            addMarker(LatLng(placeEntity!!.latitude, placeEntity!!.longitude), placeEntity!!.name)
        }
    }
}