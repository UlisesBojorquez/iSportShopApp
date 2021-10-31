package com.example.isportshop.classes

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductDataSource{
    companion object{
        fun createDataSet(): ArrayList<Product>{
            val list= ArrayList<Product>()
            val db = Firebase.firestore
            db.collection("items")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        list.add(
                            Product(
                                document.id,
                                document["name"].toString(),
                                document["description"].toString(),
                                document["price"].toString().toDouble(),
                                document["image"].toString(),
                                document["stock"].toString().toInt()
                            )
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
            return list
        }
    }
}