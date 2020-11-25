package com.example.googlemaps

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val requestNumber = 1
    private lateinit var cLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@MapsActivity)
        fab.setOnClickListener {
            currentLocation()
        }

    }

    private fun currentLocation() {
        //check the permission
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
                , requestNumber)
        }else{
            // main logic
            val task = fusedLocationProviderClient.lastLocation

            task.addOnSuccessListener { location ->

                if(location != null){
                    cLocation = location
                    Toast.makeText(applicationContext, cLocation.latitude.toString() + " "
                            + cLocation.longitude.toString(), Toast.LENGTH_SHORT).show()
                    val mapFragment = supportFragmentManager
                        .findFragmentById(R.id.map) as SupportMapFragment
                    mapFragment.getMapAsync(this)
                }
            }



        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            requestNumber -> if(grantResults.isNotEmpty() && grantResults[0]
                == PackageManager.PERMISSION_GRANTED)
                currentLocation()
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

      val myLocation = LatLng(cLocation.longitude, cLocation.latitude)
      mMap.addMarker(MarkerOptions().position(myLocation).title("My location"))
      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,8f))

        // challenge
        // add a new feature: when pressing on the map for too long it should show exact coordination and add a marker to it.
    }
}