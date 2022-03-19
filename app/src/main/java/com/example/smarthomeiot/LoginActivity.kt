package com.example.smarthomeiot

import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mGoogleApiClient: GoogleSignInClient
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        titleTV.text = "Sweet &\nSmart Home"
        subtitleTV.text = "Smart Home can change the way\nyour live in the future."

        setEvent()

        val user = auth.currentUser
        if(user != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setEvent(){

        signInRL.setOnClickListener {
            signIn()
        }
    }

    private val RC_SIGN_IN = 1
    private fun signIn() {

        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleSignIn.getClient(this, signInOptions)
        startActivityForResult(mGoogleApiClient.signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            GoogleSignIn.getSignedInAccountFromIntent(data).addOnCompleteListener {
                showLoadingDialog()

                if(it.isSuccessful){
                    val account = it.result
                    firebaseAuthWithGoogle(account.idToken!!)

                    Log.d("test", "firebaseAuthWithGoogle:" + account.id)
                }else{

                    hideLoadingDialog()
                    Log.w("test", "Google sign in failed: ", it.exception)
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {

                hideLoadingDialog()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

                Log.d("test", "signInWithCredential:success: $idToken")

            } else {
                hideLoadingDialog()

                Log.w("test", "signInWithCredential:failure", task.exception)
                Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var dialog_load: Dialog? = null
    private fun showLoadingDialog(){
        if(dialog_load == null){
            dialog_load = Dialog(this)
            dialog_load!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog_load!!.setContentView(R.layout.dialog_loading)
            dialog_load!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog_load!!.window!!.setLayout(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
            dialog_load!!.create()
        }

        dialog_load!!.show()

    }

    private fun hideLoadingDialog(){
        dialog_load!!.dismiss()
    }
}