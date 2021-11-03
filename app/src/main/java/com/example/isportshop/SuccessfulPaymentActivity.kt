package com.example.isportshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class SuccessfulPaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successful_payment)
    }

    public fun continueShopping(view : View?){
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }
}