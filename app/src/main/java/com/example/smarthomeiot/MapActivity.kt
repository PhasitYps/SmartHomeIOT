package com.example.smarthomeiot

import android.location.Location
import android.os.Bundle
import android.util.Log
import com.example.smarthomeiot.master.GPSManage
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : BaseActivity(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        initBase()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        event()

    }

    private fun event(){

        continueRL.setOnClickListener {

            val loc = mMap.cameraPosition.target

            prefs!!.floatHomeLatitude = loc.latitude.toFloat()
            prefs!!.floatHomeLongitude = loc.longitude.toFloat()
            finish()
        }

        Log.i("dsadasda", "event")

        currentFab.setOnClickListener {

            Log.i("dsadasda", "location: click")

            val gpsManage = GPSManage(this)
            gpsManage.requestGPS()
            gpsManage.setMyEvent(object : GPSManage.MyEvent{
                override fun onLocationChanged(currentLocation: Location) {
                    Log.i("dsadasda", "location: $currentLocation")

                    val current = LatLng(currentLocation.latitude, currentLocation.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 16f))
                }

                override fun onDissAccessGPS() {

                }
            })
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //13.668217, 100.614021

        val lat = prefs!!.floatHomeLatitude
        val long = prefs!!.floatHomeLongitude
        // Add a marker in Sydney and move the camera
        val thailand = LatLng(lat.toDouble(), long.toDouble())
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thailand, 16f))

    }


}