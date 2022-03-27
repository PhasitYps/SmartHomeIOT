package com.example.smarthomeiot

import androidx.appcompat.app.AppCompatActivity
import com.example.smarthomeiot.master.Prefs

open class BaseActivity: AppCompatActivity() {
    var prefs: Prefs? = null

    fun initBase(){
        prefs = Prefs(this)
    }
}