package com.example.smarthomeiot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smarthomeiot.master.Prefs
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        init()
        event()

    }

    private fun init(){

        val prefs = Prefs(this)
        when(prefs.strStatusSmart){
            "on"->{
                smartSW.isChecked = true
            }
            "off"->{
                smartSW.isChecked = false
            }
        }

        distanceEDT.setText(prefs.intDistance.toString())

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

            val prefs = Prefs(this)
            prefs.intDistance = distance.toString().toInt()
            prefs.strStatusSmart = statusSmart

            finish()
        }

        backIV.setOnClickListener {
            finish()
        }
    }
}