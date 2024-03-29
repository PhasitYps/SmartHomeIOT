package com.example.smarthomeiot.fragment

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.example.smarthomeiot.R
import com.example.smarthomeiot.SettingActivity
import com.example.smarthomeiot.master.GPSManage
import com.example.smarthomeiot.master.Prefs
import com.example.smarthomeiot.model.ModelDevice
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_smart.*
import android.content.Context

import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.*
import com.google.firebase.database.ktx.getValue


class MenuSmartFragment :Fragment(R.layout.fragment_smart){

    private var gpsManage: GPSManage? = null
    private var prefs: Prefs? = null
    private var mMap: GoogleMap? = null
    private var homeLat: Double? = null
    private var homeLng: Double? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = Prefs(requireContext())

        val database = Firebase.database
        val myRef = database.getReference("message")

        Log.d("sadasdasd", "Start App")

        //myRef.setValue("Hello, World!")
        // Read from the database
        myRef.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.getValue<String>()
                Log.d("sadasdasd", "Value is: " + value)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("sadasdasd", "Failed to read value.", error.toException())
            }

        })

        init()
        event()
    }

    override fun onDestroy() {
        super.onDestroy()
        gpsManage!!.close()
    }

    private fun init(){

        homeLat = prefs!!.floatHomeLatitude.toDouble()
        homeLng = prefs!!.floatHomeLongitude.toDouble()
        statusSmartHomeTV.text = prefs!!.strStatusSmart

        when(prefs!!.strStatusSmart){
            "on"->{
                val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
                mapFragment!!.getMapAsync { googleMap ->
                    mMap = googleMap
                    mMap!!.mapType = GoogleMap.MAP_TYPE_HYBRID
                    mMap!!.uiSettings.isMapToolbarEnabled = false
                    mMap!!.uiSettings.isScrollGesturesEnabled = false
                    mMap!!.uiSettings.isZoomGesturesEnabled = false
                    mMap!!.clear() //clear old markers

                    val deviceLoc = LatLng(homeLat!!, homeLng!!)
                    val googlePlex = CameraPosition.builder()
                        .target(deviceLoc)
                        .zoom(10f)
                        .bearing(0f)
                        .tilt(45f)
                        .build()

                    mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null)
                    mMap!!.addMarker(MarkerOptions().position(deviceLoc).icon(bitmapDescriptorFromDrawable(requireContext(), R.drawable.ic_my_home)))
                }
            }
        }

        val myRef = Firebase.database.getReference("monitor")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(sn in snapshot.children){
                    val m = sn.getValue(ModelDevice::class.java)
                    try{
                        when(m!!.name){
                            "light" ->{
                                statusLightTV.text = m.status
                            }
                            "fan"->{
                                statusFanTV.text = m.status
                            }
                            "door"->{
                                statusDoorTV.text = m.status
                            }
                        }
                    }catch (e: Exception){
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        gpsManage = GPSManage(requireActivity())
        gpsManage!!.setMyEvent(object : GPSManage.MyEvent{
            override fun onLocationChanged(local: Location) {
                //13.965766265650242, 100.58605656649073 มอรังสิต
                try {
                    //clear map
                    mMap!!.clear()

                    //set marker and camera in Map
                    val myLoc = LatLng(local.latitude, local.longitude)
                    val deviceLoc = LatLng(homeLat!!, homeLng!!)
                    mMap!!.addMarker(MarkerOptions().position(myLoc).icon(bitmapDescriptorFromDrawable(requireContext(), R.drawable.ic_car)))
                    mMap!!.addMarker(MarkerOptions().position(deviceLoc).icon(bitmapDescriptorFromDrawable(requireContext(), R.drawable.ic_my_home)))
                    val markArray = arrayListOf(myLoc, deviceLoc)
                    moveCameraMulti(mMap!!, markArray)

                    //calculate distance device to home
                    val homeLoc = Location("device")
                    homeLoc.latitude = homeLat!!
                    homeLoc.longitude = homeLng!!

                    val distance = local.distanceTo(homeLoc)
                    val distanceSetting = prefs!!.intDistance

                    displayDistanceTV.text = "Distance ${getTextDisplayDistance(distance)}"

                    if(distance <= distanceSetting){
                        changeStatusIOT("light", "on")
                        changeStatusIOT("fan", "on")
                        changeStatusIOT("door", "on")

                        if(statusLightTV.text.equals("off") || statusFanTV.text.equals("off") || statusDoorTV.text.equals("off")){
                            Toast.makeText(requireContext(), "IOT is on", Toast.LENGTH_SHORT).show()
                        }

                    }else{
                        changeStatusIOT("light", "off")
                        changeStatusIOT("fan", "off")
                        changeStatusIOT("door", "off")

                        if(statusLightTV.text.equals("on") || statusFanTV.text.equals("on") || statusDoorTV.text.equals("on")){
                            Toast.makeText(requireContext(), "IOT is off", Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (e: Exception){}
            }
            override fun onDissAccessGPS() {}
        })

        when(prefs!!.strStatusSmart){
            "on"->{
                gpsManage!!.requestGPS()
            }
            "off"->{
                displayDistanceTV.text = "--//--"
                gpsManage!!.close()
            }
        }

    }

    private fun event(){

        Log.i("dsadasda", "event")
        settingIV.setOnClickListener {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

    }

    private fun bitmapFromDrawable(context: Context, id: Int): Bitmap{
        val bitmap = BitmapFactory.decodeResource(context.resources, id)
        return bitmap
    }

    private fun bitmapDescriptorFromDrawable(context: Context, resId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, resId)
        vectorDrawable!!.setBounds(0, 0, 100, 100)
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun moveCameraMulti(mMap: GoogleMap, list: ArrayList<LatLng>) {
        val bc = LatLngBounds.Builder()
        if (list.size != 0) {
            for (i in list.indices) {
                val item = list[i]
                bc.include(item)
            }

            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 150), 5000, null)
        }
    }

    private fun getTextDisplayDistance(distanceTo: Float): String? {

        var distance = distanceTo
        var dist = ""

        if (distance > 1000.0f) {
            distance /= 1000.0f
            dist = "${String.format("%.3f", distance)} KM"
        }else{
            dist = "${String.format("%.3f", distance)} M"
        }

        return dist
    }

    private fun changeStatusIOT(device: String, status: String){

        val model = ModelDevice()
        model.name = device
        model.status = status

        val myRef = Firebase.database.getReference("monitor")
        myRef.child(device).setValue(model)
    }


}