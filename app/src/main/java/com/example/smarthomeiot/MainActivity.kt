package com.example.smarthomeiot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.smarthomeiot.fragment.MenuHomeFragment
import com.example.smarthomeiot.fragment.MenuSmartFragment
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBase()

        when(prefs!!.strPageDefault){
            "home"->{
                changeMenu("home")
            }
            "smart"->{
                changeMenu("smart")
            }
        }

        event()
    }

    private fun event(){

        menu_homeRL.setOnClickListener {
            prefs!!.strPageDefault = "home"
            changeMenu("home")
        }

        menu_smartRL.setOnClickListener {
            prefs!!.strPageDefault = "smart"
            changeMenu("smart")
        }
    }

    private var fragmentCurrent = ""
    private fun changeMenu(menu: String) {
        fragmentCurrent = menu

        when (menu) {
            "home" -> {
                menuHomeIV.setBackgroundResource(R.drawable.ic_home_filled)
                menuSmartIV.setBackgroundResource(R.drawable.ic_light)

                menuHomeIV.background.setTint(ContextCompat.getColor(this, R.color.black))

                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, MenuHomeFragment())
                    commit()
                }

            }
            "smart" -> {
                menuHomeIV.setBackgroundResource(R.drawable.ic_home)
                menuSmartIV.setBackgroundResource(R.drawable.ic_light_filled)

                menuSmartIV.background.setTint(ContextCompat.getColor(this, R.color.black))

                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, MenuSmartFragment())
                    commit()
                }
            }
        }
    }
}