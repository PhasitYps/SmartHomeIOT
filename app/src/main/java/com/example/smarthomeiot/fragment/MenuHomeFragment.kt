package com.example.smarthomeiot.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.bumptech.glide.Glide
import com.example.smarthomeiot.R
import com.example.smarthomeiot.model.ModelDevice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*

class MenuHomeFragment :Fragment(R.layout.fragment_home) {

    private val auth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        event()
    }

    private fun init(){

        val user = auth.currentUser
        usernameTV.text = "${user.displayName}!"
        Glide.with(requireActivity()).load(user.photoUrl).into(profileUserIV)

        val myRef = Firebase.database.getReference("monitor")
        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(sn in snapshot.children){
                    val m = sn.getValue(ModelDevice::class.java)

                    try{
                        when(m!!.name){

                            "light" ->{
                                when(m.status){
                                    "on"->{
                                        lightSw.isChecked = true
                                    }
                                    "off"->{
                                        lightSw.isChecked = false
                                    }
                                }
                            }

                            "fan"->{
                                when(m.status){
                                    "on"->{
                                        fanSw.isChecked = true
                                    }
                                    "off"->{
                                        fanSw.isChecked = false
                                    }
                                }
                            }
                            "door"->{
                                when(m.status){
                                    "on"->{
                                        doorSw.isChecked = true
                                    }
                                    "off"->{
                                        doorSw.isChecked = false
                                    }
                                }
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


    }

    private fun event(){

        comebackLL.setOnClickListener {
            lightSw.isChecked = true
            fanSw.isChecked = true
            doorSw.isChecked = true

            changeStatusIOT("light", "on")
            changeStatusIOT("fan", "on")
            changeStatusIOT("door", "on")
        }

        leaveLL.setOnClickListener {
            lightSw.isChecked = false
            fanSw.isChecked = false
            doorSw.isChecked = false

            changeStatusIOT("light", "off")
            changeStatusIOT("fan", "off")
            changeStatusIOT("door", "off")
        }

        lightSw.setOnClickListener {
            when(lightSw.isChecked){
                true->{
                    changeStatusIOT("light", "on")
                }
                false->{
                    changeStatusIOT("light", "off")
                }
            }
        }

        fanSw.setOnClickListener {
            when(fanSw.isChecked){
                true->{
                    changeStatusIOT("fan", "on")
                }
                false->{
                    changeStatusIOT("fan", "off")
                }
            }
        }

        doorSw.setOnClickListener {
            when(doorSw.isChecked){
                true->{
                    changeStatusIOT("door", "on")
                }
                false->{
                    changeStatusIOT("door", "off")
                }
            }
        }
    }

    private fun changeStatusIOT(device: String, status: String){

        val model = ModelDevice()
        model.name = device
        model.status = status

        val myRef = Firebase.database.getReference("monitor")
        myRef.child(device).setValue(model)
    }

}