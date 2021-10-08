package com.example.isportshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecoverPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var emailRecover : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)

        // Initialize Firebase Auth
        auth = Firebase.auth
        //Initialize inputs
        emailRecover = findViewById(R.id.txtEmailRecover)
    }

    public fun RecoverPassword(v: View?){
        if(!TextUtils.isEmpty(emailRecover.text.toString())){
            auth.sendPasswordResetEmail(emailRecover.text.toString())
                .addOnCompleteListener(this){
                    if(it.isSuccessful){
                        Log.d("INPUTS","The password was sent.")
                        Toast.makeText(this,"Password send", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Log.d("FIREBASE","The email is incorrect in recovery password.")
                        Toast.makeText(this,"Incorrect email", Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
            Log.w("INPUTS","The email is missing in recover password.")
            Toast.makeText(this,"EMAIL MISSING", Toast.LENGTH_SHORT).show()
        }

    }
}