package com.example.isportshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

            //Date
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date()) //03/12/2021 06:41:32
            //TotalAmount
            val totalAmountSpent = intent.getStringExtra("totalAmount")
            //id
            var idshopHistory = currentDate + "_" + totalAmountSpent
            Log.d("iiiiiiiiiiiiiiiiiid", idshopHistory)
            //items
            val allItems = intent.getStringExtra("allItems")

            Log.d("eeeeeeeeeeeeee", allItems.toString())


            //add to bd shopHistory
            addShopHistoryToBD()

            //--------------Vaciar carrito

            val user = Firebase.auth.currentUser
            user?.let {
                for(provider in it.providerData){
                    // Id of the provider (ex: google.com)
                    val providerId = provider.providerId

                    // UID specific to the provider
                    val uid = provider.uid

                    val email = provider.email

                    var listProduct = mutableMapOf<String, Number>()

                    Firebase.firestore.collection("users").get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                if(document["email"].toString().equals(email)) {
                                    /*listProduct = document["cartItems"] as MutableMap<String, Number>
                                    */
                                        //allItems = listProduct.keys
                                    listProduct.clear()
                                }
                            }
                            val stringEmail = email.toString()
                            val cartItemsRef = Firebase.firestore.collection("users").document(stringEmail)

                            // Set the "isCapital" field of the city 'DC'
                            cartItemsRef
                                .update("cartItems", listProduct)
                                .addOnSuccessListener { Log.d("Res", "DocumentSnapshot successfully updated!") }
                                .addOnFailureListener { e -> Log.w("Res", "Error updating document", e) }
                        }
                }
            }
            //----------------------Vaciar carrito termina
            Log.d("Miraaaaaaa si ", idshopHistory)
            Log.d("Miraaaa 2 ", allItems.toString())
            //Log.d("allllll iteeeems", allItems.toString())
            val intent = Intent(this, SuccessfulPaymentActivity::class.java)
            startActivity(intent)

        }else{
            Toast.makeText(this, "Payment data is incomplete", Toast.LENGTH_SHORT).show()
        }
    }

    public fun addShopHistoryToBD(view : View?){
        AlertDialog.Builder(this)
            .setMessage("Added to your cart")
            .setPositiveButton("OK") { p0, p1 ->
            }
            .create()
            .show()

        val user = Firebase.auth.currentUser
        user?.let {
            for(provider in it.providerData){
                // Id of the provider (ex: google.com)
                val providerId = provider.providerId

                // UID specific to the provider
                val uid = provider.uid

                val email = provider.email

                var listProduct = mutableMapOf<String, Number>()

                Firebase.firestore.collection("users").get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            if(document["email"].toString().equals(email)) {
                                listProduct = document["cartItems"] as MutableMap<String, Number>
                                if(listProduct.containsKey(productName)){
                                    var productAmount : Number = listProduct.getValue(productName)
                                    var productAmountInt : Int = productAmount.toInt()
                                    productAmountInt++
                                    productAmount = productAmountInt
                                    listProduct.put(productName, productAmount)
                                }else{
                                    var productAmount : Number = 1
                                    listProduct.put(productName, productAmount)
                                }
                            }
                        }
                        val stringEmail = email.toString()
                        val cartItemsRef = Firebase.firestore.collection("users").document(stringEmail)

                        // Set the "isCapital" field of the city 'DC'
                        cartItemsRef
                            .update("cartItems", listProduct)
                            .addOnSuccessListener { Log.d("Res", "DocumentSnapshot successfully updated!") }
                            .addOnFailureListener { e -> Log.w("Res", "Error updating document", e) }
                    }
            }
        }

        if((stock.text.toString().split(" ")[1].toInt()-1) > 0){
            stock.setText("Stock: " + (stock.text.toString().split(" ")[1].toInt()-1).toString())

            val db = Firebase.firestore
            db.collection("items").document(intent.getStringExtra("id").toString())
                .update("stock", stock.text.toString().split(" ")[1].toInt())
                .addOnSuccessListener { document ->
                    Log.d("STOCK", "Update went well")
                }
                .addOnFailureListener { e ->
                    Log.wtf("STOCK", "Error on read the document", e)
                }
        }
    }
}