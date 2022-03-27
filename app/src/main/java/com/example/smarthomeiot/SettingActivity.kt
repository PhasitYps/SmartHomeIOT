package com.example.smarthomeiot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smarthomeiot.master.Prefs
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
}