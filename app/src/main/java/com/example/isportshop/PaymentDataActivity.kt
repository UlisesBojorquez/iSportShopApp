package com.example.isportshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast

class PaymentDataActivity : AppCompatActivity() {
    lateinit var number : EditText
    lateinit var date : EditText
    lateinit var cvv : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_data)
        number = findViewById(R.id.edPayNumCard)
        date = findViewById(R.id.etPayDateCard)
        cvv = findViewById(R.id.edPayCvvCard)
    }

    private fun ValidateInputsPayment():Boolean{
        var flag = true

        if(TextUtils.isEmpty(number.text.toString())){
            flag = false
        }
        if(TextUtils.isEmpty(date.text.toString())){
            flag = false
        }
        if(TextUtils.isEmpty(cvv.text.toString())){
            flag = false
        }

        return flag
    }

    public fun toPay(view : View?){
        if(ValidateInputsPayment()){
            Toast.makeText(this, "Succesful Payment", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(this, "Payment data is incomplete", Toast.LENGTH_SHORT).show()

    }
}