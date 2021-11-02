package com.example.isportshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class PaymentDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_data)
    }

    public fun toPay(view : View?){
        Toast.makeText(this, "Succesful Payment", Toast.LENGTH_SHORT).show()
    }
}