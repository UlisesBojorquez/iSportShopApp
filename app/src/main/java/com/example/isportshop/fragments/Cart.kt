package com.example.isportshop.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.isportshop.PaymentDataActivity
import com.example.isportshop.R
import com.example.isportshop.classes.ProductCart
import com.example.isportshop.classes.ProductsAdapterCart
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Cart.newInstance] factory method to
 * create an instance of this fragment.
 */
class Cart : Fragment() {
    var itemsList = mutableMapOf<String, Number>()
    var allItems = arrayListOf<String>()
    private var userEmail: String? = null
    var total = 0.0
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //lateinit var paymentDataFragment: PaymentData
    lateinit var btnToShop : Button
    lateinit var btnShopHistory : Button
    lateinit var tvTotalAmount : TextView


    lateinit var recyclerView : RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        total = 0.0
        btnToShop = view.findViewById(R.id.btnToShop)
        btnShopHistory = view.findViewById(R.id.btnShopHistory)
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount)

        btnToShop.setOnClickListener {
            val intent = Intent(context, PaymentDataActivity::class.java)
            intent.putExtra("totalAmount",total.toString())
            intent.putExtra("allItems",allItems.toString())
            startActivity(intent)
        }

        btnShopHistory.setOnClickListener {
            //val intent = Intent(context, ShopHistoryActivity::class.java)
            //startActivity(intent)
            val shopFragment = ShopHistory()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment_container, shopFragment)
            transaction.commit()

            var bundle=Bundle()
            bundle.putString("userCart",userEmail)
            shopFragment.arguments = bundle
        }

        arguments?.let {

            if(it.containsKey("userCart")){
                Log.d("fragment",it.getString("userCart").toString())
                //Obtain info from the database
                var doc=it.getString("userCart").toString()
                //Log.d("Veeeeeeeeeeeeeeeeer  111", doc)
                userEmail = it.getString("userCart").toString()
                val db = Firebase.firestore
                db.collection("users").document(doc)
                    .addSnapshotListener{ document, e ->

                        if(e != null){
                            Log.e("FIRESTORE", "error: $e")
                            Log.w("FIREBASE", "Error on read the document from firestore", e)
                            return@addSnapshotListener
                        }

                        var data = document?.data
                        Log.d("PROFILE", "${data.toString()}")

                        this.itemsList = document?.get("cartItems") as MutableMap<String, Number>

                    }
            }
        }


        recyclerView=view.findViewById(R.id.recycler_view_cart)
        gridLayoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = gridLayoutManager
        var listProduct = arrayListOf<ProductCart>()
        val db = Firebase.firestore


        //Obtener items de la bd

        Firebase.firestore.collection("items")
            .addSnapshotListener{ documents, e ->
                if(e != null){
                    Log.w(ContentValues.TAG, "Error getting documents from Firestrore: ", e)
                    return@addSnapshotListener
                }

                for (document in documents!!.iterator()) {
                    if(!listProduct.contains(ProductCart(
                            document["name"].toString(),
                            document["description"].toString(),
                            document["price"].toString().toDouble(),
                            document["image"].toString(),
                            document["stock"].toString().toInt()
                        ))
                    ) {
                        var nameDocument = document["name"].toString()
                        for (item in this.itemsList) {
                            if (nameDocument.equals(item.key)) {  //Si no funciona, cambiar aqui/
                                listProduct.add(
                                    ProductCart(
                                        document["name"].toString(),
                                        document["description"].toString(),
                                        document["price"].toString().toDouble(),
                                        document["image"].toString(),
                                        document["stock"].toString().toInt()
                                    )
                                )
                                allItems.add(document["name"].toString())
                                total += document["price"].toString().toDouble()
                            }
                        }
                    }
                }
                tvTotalAmount.setText(total.toString())

                recyclerView.adapter = ProductsAdapterCart(listProduct)
                Log.d(ContentValues.TAG, "Successful GET of products on names")
            }
    }


    companion object {
        //private const val TAG_FRAGMENTO = "fragmentito"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Cart().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}