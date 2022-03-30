package com.example.smarthomeiot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smarthomeiot.master.Prefs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initBase()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



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

            finish()
        }

        backIV.setOnClickListener {
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.uiSettings.isScrollGesturesEnabled = false
        mMap.uiSettings.isZoomGesturesEnabled = false
        mMap.setOnMapClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}