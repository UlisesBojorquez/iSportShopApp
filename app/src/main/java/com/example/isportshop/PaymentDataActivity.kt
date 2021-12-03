package com.example.isportshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
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
            var allItems = intent.getStringExtra("allItems")
            if (allItems != null) {
                allItems = allItems.substring(1, allItems.length-1)
            }
            var allItemsList = allItems?.split(", ")


            Log.d("eeeeeeeeeeeeee", allItems.toString())
            Log.d("eeeeeeeeeeeeee  list", allItemsList.toString())


            //add to bd shopHistory
            addShopHistoryToBD(idshopHistory, allItemsList as ArrayList<String>?)

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

    public fun addShopHistoryToBD(idshopHistory: String, allItems: ArrayList<String>?){

        val user = Firebase.auth.currentUser
        user?.let {
            for(provider in it.providerData){
                // Id of the provider (ex: google.com)
                val providerId = provider.providerId

                // UID specific to the provider
                val uid = provider.uid

                val email = provider.email

                var listProduct = mutableMapOf<String, ArrayList<String>>()

                Firebase.firestore.collection("users").get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            if(document["email"].toString().equals(email)) {
                                listProduct = document["shopHistory"] as MutableMap<String,  ArrayList<String>>
                                if(listProduct.containsKey(idshopHistory)){

                                }else{
                                    if (allItems != null) {
                                        listProduct.put(idshopHistory, allItems)
                                    }
                                }
                            }
                        }
                        val stringEmail = email.toString()
                        val cartItemsRef = Firebase.firestore.collection("users").document(stringEmail)

                        // Set the "isCapital" field of the city 'DC'
                        cartItemsRef
                            .update("shopHistory", listProduct)
                            .addOnSuccessListener { Log.d("Res", "DocumentSnapshot successfully updated shopHistory!") }
                            .addOnFailureListener { e -> Log.w("Res", "Error updating document shopHistory", e) }
                    }
            }
        }
    }
}