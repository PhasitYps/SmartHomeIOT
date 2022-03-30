package com.example.smarthomeiot

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smarthomeiot.master.GPSManage
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //13.668217, 100.614021

        // Add a marker in Sydney and move the camera
        val thailand = LatLng(13.668217, 100.614021)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thailand, 16f))

        initMap()
    }

    private fun initMap(){

        val gpsManage = GPSManage(this)
        gpsManage.requestGPS()
        gpsManage.setMyEvent(object : GPSManage.MyEvent{
            override fun onLocationChanged(currentLocation: Location) {

                val current = LatLng(currentLocation.latitude, currentLocation.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 16f))
            }

            override fun onDissAccessgps() {

            }
        })
    }
}