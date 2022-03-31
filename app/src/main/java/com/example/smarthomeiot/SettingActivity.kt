package com.example.smarthomeiot

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.smarthomeiot.master.GPSManage
import com.example.smarthomeiot.master.Prefs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initBase()


        init()
        event()

    }

    private fun init(){

        when(prefs!!.strStatusSmart){
            "on"->{
                smartSW.isChecked = true
            }
            "off"->{
                smartSW.isChecked = false
            }
        }

        distanceEDT.setText(prefs!!.intDistance.toString())

    }

    private fun event(){

        mapLL.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)

        }

        continueRL.setOnClickListener {
            val distance = if(distanceEDT.text.isEmpty()){
                0
            }else{
                distanceEDT.text.toString().toInt()
            }
            val statusSmart = if(smartSW.isChecked){
                "on"
            }else{
                "off"
            }

            prefs!!.intDistance = distance.toString().toInt()
            prefs!!.strStatusSmart = statusSmart

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()

        }

        backIV.setOnClickListener {
            finish()
        }
    }

    /*override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        *//*mMap.uiSettings.isScrollGesturesEnabled = false
        mMap.uiSettings.isZoomGesturesEnabled = false*//*



        val lat = prefs!!.longLatitude
        val long = prefs!!.longLongitude
        // Add a marker in Sydney and move the camera
        val thailand = LatLng(lat.toDouble(), long.toDouble())
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
    }*/

}