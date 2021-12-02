package com.example.isportshop

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.isportshop.classes.ProductCart
import com.example.isportshop.classes.ProductsAdapterCart
import com.example.isportshop.classes.Shop
import com.example.isportshop.classes.ShopHistoryAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShopHistoryActivity : AppCompatActivity() {
    var itemsList = arrayListOf<String>()
    lateinit var recyclerView : RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_history)
    }

    /*
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }
    */


     fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)

        recyclerView=view.findViewById(R.id.recycler_view_cart)
        //gridLayoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = gridLayoutManager
        var listShops = arrayListOf<Shop>()


        Firebase.firestore.collection("users")
            .addSnapshotListener{ documents, e ->

                if(e != null){
                    Log.w(ContentValues.TAG, "Error getting documents from Firestrore: ", e)
                    return@addSnapshotListener
                }

                for (document in documents!!.iterator()) {
                    if(!listShops.contains(Shop(
                            document["date"].toString(),
                            document["totalAmount"].toString().toDouble(),
                        ))
                    ) {
                        var nameDocument = document["name"].toString()
                        for (item in this.itemsList) {
                            //Log.d("entroooooooooo", item)
                            if (nameDocument.equals(item)) {  //Si no funciona, cambiar aqui/
                                listShops.add(
                                    Shop(
                                        document["date"].toString(),
                                        document["totalAmount"].toString().toDouble(),
                                    )
                                )
                            }
                        }
                    }


                }
                recyclerView.adapter = ShopHistoryAdapter(listShops)
                Log.d(ContentValues.TAG, "Successful GET of products on names")
            }



        recyclerView.adapter =  ShopHistoryAdapter(listShops)
    }
}