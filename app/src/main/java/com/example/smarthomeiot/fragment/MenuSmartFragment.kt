package com.example.smarthomeiot.fragment

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.example.smarthomeiot.R
import com.example.smarthomeiot.SettingActivity
import com.example.smarthomeiot.master.GPSManage
import com.example.smarthomeiot.master.Prefs
import com.example.smarthomeiot.model.ModelDevice
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_smart.*


class MenuSmartFragment :Fragment(R.layout.fragment_smart) {

    private var gpsManage: GPSManage? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        event()
    }

    override fun onResume() {
        super.onResume()

        val prefs = Prefs(requireContext())
        when(prefs.strStatusSmart){
            "on"->{
                gpsManage!!.requestGPS()
            }
            "off"->{
                displayDistanceTV.text = "--//--"
                gpsManage!!.close()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gpsManage!!.close()
    }

    private fun init(){

        val prefs = Prefs(requireContext())

        val myRef = Firebase.database.getReference("monitor")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
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

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        gpsManage = GPSManage(requireActivity())
        gpsManage!!.setMyEvent(object : GPSManage.MyEvent{
            override fun onLocationChanged(local: Location) {
                //13.965766265650242, 100.58605656649073 มอรังสิต
                val homeLoc = Location("device")
                homeLoc.latitude = 13.965766265650242
                homeLoc.longitude = 100.58605656649073

                displayDistanceTV.text = "distance "+ getDistance(local, homeLoc)

                val distance = local.distanceTo(homeLoc)
                val distanceInSetting = prefs.intDistance

                if(distance <= distanceInSetting){
                    changeStatusIOT("light", "on")
                    changeStatusIOT("fan", "on")
                    changeStatusIOT("door", "on")

                    if(statusLightTV.text.equals("off") || statusFanTV.text.equals("off") || statusDoorTV.text.equals("off")){
                        try {
                            statusLightTV.text = "on"
                            statusFanTV.text = "on"
                            statusDoorTV.text = "on"

                            Toast.makeText(requireContext(), "IOT is on", Toast.LENGTH_SHORT).show()
                        }catch (e: Exception){

                        }
                    }

                }else{
                    changeStatusIOT("light", "off")
                    changeStatusIOT("fan", "off")
                    changeStatusIOT("door", "off")

                    if(statusLightTV.text.equals("on") || statusFanTV.text.equals("on") || statusDoorTV.text.equals("on")){
                        try {
                            statusLightTV.text = "off"
                            statusFanTV.text = "off"
                            statusDoorTV.text = "off"

                            Toast.makeText(requireContext(), "IOT is off", Toast.LENGTH_SHORT).show()
                        }catch (e: Exception){

                        }
                    }
                }


            }

            override fun onDissAccessgps() {

            }
        })

        when(prefs.strStatusSmart){
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

        settingIV.setOnClickListener {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

        /*autoSmartSw.setOnClickListener {
            when(autoSmartSw.isChecked){
                true->{
                    gpsManage!!.requestGPS()
                }
                false->{
                    gpsManage!!.close()
                }
            }
        }*/
    }

    private fun getDistance(start: Location, end: Location): String? {

        var distance = start.distanceTo(end)
        var dist = ""

        if (distance > 1000.0f) {
            distance /= 1000.0f
            dist = "$distance KM"
        }else{
            dist = "$distance M"
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