package com.example.isportshop.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.isportshop.R
import com.example.isportshop.classes.ProductCart

import com.example.isportshop.classes.Shop
import com.example.isportshop.classes.ShopHistoryAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ShopHistory : Fragment() {

    //var itemsList = mutableMapOf<String, Number>()
    var itemsList = mutableMapOf<String, ArrayList<String>>() //borrar
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_shop_history, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            if(it.containsKey("userCart")){
                //Obtain info from the database
                var doc=it.getString("userCart").toString()
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

                        //name.text = document["name"].toString()

                        //-------------------------------------------------------

                        recyclerView=view.findViewById(R.id.recycler_view_shop_history)
                        gridLayoutManager = GridLayoutManager(context, 1)
                        recyclerView.layoutManager = gridLayoutManager
                        var listShops = arrayListOf<Shop>()


                        //Obtener items de la bd

                        for (item in itemsList){
                            var list = item.key.split("_")
                            listShops.add(
                                Shop(
                                    list.get(0),
                                    list.get(1).toDouble(),
                                )
                            )
                        }
                        recyclerView.adapter = ShopHistoryAdapter(listShops)
                        Log.d(ContentValues.TAG, "Successful GET of products on names")

                        //-------------------------------------------------------
                    }
            }
        }
    }


    companion object {
        //private const val TAG_FRAGMENTO = "fragmentito"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShopHistory().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}