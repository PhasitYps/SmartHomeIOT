package com.example.smarthomeiot.master

import android.content.Context
import android.content.SharedPreferences

class Prefs (private var context: Context)
{
    private val APP_PREF_INT_DISTANCE = "intDistance"
    private val APP_PREF_STR_STATUS_SMART = "strStatusSmart"
    private val APP_PREF_LONG_LATITUDE = "doubleLatitude"
    private val APP_PREF_LONG_LONGITUDE = "doubleLongitude"

    private var preferences: SharedPreferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE)

    var intDistance: Int
        get() = preferences.getInt(APP_PREF_INT_DISTANCE, 500)
        set(value) = preferences.edit().putInt(APP_PREF_INT_DISTANCE, value).apply()

    var strStatusSmart: String
        get() = preferences.getString(APP_PREF_STR_STATUS_SMART, "off").toString()
        set(value) = preferences.edit().putString(APP_PREF_STR_STATUS_SMART, value).apply()

    var longLatitude: Float
        get() = preferences.getFloat(APP_PREF_LONG_LATITUDE, 13.668217f)
        set(value) = preferences.edit().putFloat(APP_PREF_LONG_LATITUDE, value).apply()

    var longLongitude: Float
        get() = preferences.getFloat(APP_PREF_LONG_LONGITUDE, 100.614021f)
        set(value) = preferences.edit().putFloat(APP_PREF_LONG_LONGITUDE, value).apply()


}