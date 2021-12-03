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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProductShopHistoryActivity : AppCompatActivity() {

    lateinit var dateS : TextView
    lateinit var totalAmountS : TextView
    var idShop = ""
    var email = ""

    var items = mutableMapOf<String, ArrayList<String>>()
    var itemsList = ArrayList<String>()
    var listShopItems = arrayListOf<ProductCart>()
    lateinit var recyclerView : RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_shop_history)

        dateS=findViewById(R.id.tvProductShopDate)
        totalAmountS=findViewById(R.id.tvProductShopPrice)

        dateS.setText(intent.getStringExtra("date"))
        //Picasso.get().load(intent.getStringExtra("image")).into(image)
        totalAmountS.setText("Price: $" + intent.getStringExtra("totalAmount").toString())

        idShop = intent.getStringExtra("date").toString() + "_" + intent.getStringExtra("totalAmount").toString()

        //---------------Recycler view
        //---------------------------


        val user = Firebase.auth.currentUser
        user?.let {
            for (provider in it.providerData) {
                email = provider.email.toString()
            }
        }

        //var doc=it.getString("userCart").toString()
        var doc=email.toString()
        //var doc=intent.getStringExtra("userCart").toString()
        val db = Firebase.firestore


        db.collection("users").document(doc)
            .addSnapshotListener{ document, e ->
                if(e != null){
                    Log.e("FIRESTORE", "error: $e")
                    Log.w("FIREBASE", "Error on read the document from firestore", e)
                    return@addSnapshotListener
                }

                this.items = document?.get("shopHistory") as MutableMap<String, ArrayList<String>>


                //Obtener items de la bd
                for (item in items){
                    if(item.key.equals(idShop)){
                        for(item1 in item.value){
                            //var values = item.value.toString()
                            //var valuesSub = values.subSequence(1, values.length-1)
                            //itemsList.add(valuesSub.toString())
                            itemsList.add(item1)
                        }

                    }
                }
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


                        for (item in itemsList) {
                            if (nameDocument.equals(item)) {  //Si no funciona, cambiar aqui/
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
                Log.d(ContentValues.TAG, "Successful GET of products on names final")
            }
    }
}