package com.dicoding.picodiploma.loginwithanimation.view.map

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.dicoding.picodiploma.loginwithanimation.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMapsBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun loadData() {
        viewModel.getSession().observe(this, Observer {user->
            val token = "Bearer "+user.token
            viewModel.getAllStoriesWithMarker(token)
            viewModel.allStories.observe(this, Observer {
                Log.d("STORY MARKER","$it")
                it?.forEach { data ->
                    val latLng = data.lat?.let { it1 -> data.lon?.let { it2 -> LatLng(it1, it2) } }
                    latLng?.let{
                        Glide.with(this@MapsActivity)
                            .asBitmap()
                            .load(data.photoUrl)
                            .transform(CircleCrop()) // Mengubah gambar menjadi lingkaran
                            .into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    // Menggunakan gambar lingkaran sebagai marker icon
                                    val resizedBitmap = Bitmap.createScaledBitmap(resource, 100, 100, false)
                                    val markerIcon = BitmapDescriptorFactory.fromBitmap(resizedBitmap)
                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(latLng)
                                            .title(data.name)
                                            .snippet(data.description)
                                            .icon(markerIcon) // Set custom icon
                                    )
                                }
                            })
                    }

                }
            })
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        // Add a marker in Sydney and move the camera
        loadData()
        val jawa = LatLng(-7.5000, 110.5000)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(jawa, 4f)
        mMap.moveCamera(cameraUpdate)
    }
}