package com.example.isportshop

import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.isportshop.classes.Product
import com.example.isportshop.classes.ProductsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProductActivity : AppCompatActivity() {

    lateinit var nameP : TextView
    lateinit var image : ImageView
    lateinit var description : TextView
    lateinit var price : TextView
    lateinit var stock : TextView
    var productName = ""
    var itemsList = arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_product)

        nameP=findViewById(R.id.product_name_detail)
        image=findViewById(R.id.product_image_detail)
        description=findViewById(R.id.product_description_detail)
        price=findViewById(R.id.product_price_detail)
        stock=findViewById(R.id.product_stock_detail)


        nameP.setText(intent.getStringExtra("name"))
        Picasso.get().load(intent.getStringExtra("image")).into(image)
        description.setText(intent.getStringExtra("description"))
        price.setText("Price: $" + intent.getStringExtra("price"))
        stock.setText("Stock: " + intent.getStringExtra("stock"))

        productName = intent.getStringExtra("name").toString()

    }


    public fun addToCart(v: View?){
        AlertDialog.Builder(this)
            .setMessage("Added to your wish list")
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
    }
}