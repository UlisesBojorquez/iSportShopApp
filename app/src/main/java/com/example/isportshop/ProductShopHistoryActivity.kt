package com.example.isportshop

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.isportshop.classes.ProductCart
import com.example.isportshop.classes.ProductsAdapterCart
import com.example.isportshop.classes.Shop
import com.example.isportshop.classes.ShopHistoryAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProductShopHistoryActivity : AppCompatActivity() {

    lateinit var dateS : TextView
    lateinit var totalAmountS : TextView
    //lateinit var items : TextView
    //var = arrayListOf<String>()

    var itemsList = mutableMapOf<String, ArrayList<String>>() //borrar
    lateinit var recyclerView : RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_product_shop_history)

        dateS=findViewById(R.id.tvProductShopDate)
        totalAmountS=findViewById(R.id.tvProductShopPrice)
        Log.d("Miraaaaaaaaa1", intent.getStringExtra("date").toString())

        Log.d("Miraaaaaaaaa1", intent.getStringExtra("totalAmount").toString())

        dateS.setText(intent.getStringExtra("date"))
        //Picasso.get().load(intent.getStringExtra("image")).into(image)
        //totalAmountS.setText(intent.getStringExtra("date"))
        totalAmountS.setText("Price: $" + intent.getStringExtra("totalAmount").toString())

        //---------------Recycler view
        recyclerView = findViewById(R.id.recycler_view_shop_history_items)
        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager

        //---------------------------
        //var doc=it.getString("userCart").toString()
        var doc=intent.getStringExtra("userCart").toString()
        val db = Firebase.firestore

        db.collection("users").document(doc)
            .addSnapshotListener{ document, e ->
                if(e != null){
                    Log.e("FIRESTORE", "error: $e")
                    Log.w("FIREBASE", "Error on read the document from firestore", e)
                    return@addSnapshotListener
                }
                var data = document?.data
                //Log.d("PROFILE", "${data.toString()}")
                this.itemsList = document?.get("shopHistory") as MutableMap<String, ArrayList<String>>



                var listShopItems = arrayListOf<ProductCart>()


                //Obtener items de la bd

                for (item in itemsList){
                    var list = item.key.split("_")
                    listShopItems.add(
                        ProductCart(
                            document["name"].toString(),
                            document["description"].toString(),
                            document["price"].toString().toDouble(),
                            document["image"].toString(),
                            document["stock"].toString().toInt()
                        )
                    )

                }


                //-------------------------------------------------------
            }

        recyclerView = findViewById(R.id.recycler_view_shop_history_items)
        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager

        Firebase.firestore.collection("items")
            .addSnapshotListener{ documents, e ->

                if(e != null){
                    Log.w(ContentValues.TAG, "Error getting documents from Firestrore: ", e)
                    return@addSnapshotListener
                }

                for (document in documents!!.iterator()) {
                    if(!listShopItems.contains(ProductCart(
                            document["name"].toString(),
                            document["description"].toString(),
                            document["price"].toString().toDouble(),
                            document["image"].toString(),
                            document["stock"].toString().toInt()
                        ))
                    ) {
                        var nameDocument = document["name"].toString()
                        for (item in this.itemsList) {
                            Log.d("entroooooooooo", "-")
                            if (nameDocument.equals(item.key)) {  //Si no funciona, cambiar aqui/
                                listShopItems.add(
                                    ProductCart(
                                        document["name"].toString(),
                                        document["description"].toString(),
                                        document["price"].toString().toDouble(),
                                        document["image"].toString(),
                                        document["stock"].toString().toInt()
                                    )
                                )
                            }
                        }
                    }
                }
                recyclerView.adapter = ProductsAdapterCart(listShopItems)
                Log.d(ContentValues.TAG, "Successful GET of products on names")
            }

    }


}