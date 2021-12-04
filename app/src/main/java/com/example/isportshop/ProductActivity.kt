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

        val db = Firebase.firestore
        db.collection("items").document(intent.getStringExtra("id").toString())
            .get()
            .addOnSuccessListener { document ->
                var data = document?.data
                nameP.setText(document["name"].toString())
                Picasso.get().load(document["image"].toString()).into(image)
                description.setText(document["description"].toString())
                price.setText("Price: $" + document["price"].toString())
                stock.setText("Stock: " + document["stock"].toString())
                productName = document["name"].toString()
            }
            .addOnFailureListener { e ->
                Log.w("FIREBASE", "Error on read the document", e)
            }
    }

    public fun addToCart(v: View?){
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