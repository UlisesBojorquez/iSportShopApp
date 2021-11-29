package com.example.isportshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SuccessfulPaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successful_payment)
    }

    public fun toContinueShopping(view : View?){
        //--------------Vaciar carrito
        val user = Firebase.auth.currentUser
        user?.let {
            for(provider in it.providerData){
                val email = provider.email

                val intent=Intent(this,MenuActivity::class.java)
                intent.putExtra("userInfo",email)
                startActivity(intent)
            }
        }
        
            
    }
}