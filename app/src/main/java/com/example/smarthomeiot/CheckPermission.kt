package com.example.smarthomeiot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class CheckPermission : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_permission)


        timerCount()

    }

    var T: Timer? = null
    var count = 0
    private fun timerCount() {
        T = Timer()
        T!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (count == 1) {

                        val user = auth.currentUser
                        if(user != null){
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    }
                    count++
                }
            }
        }, 0, 500)
    }
}