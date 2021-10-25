package com.example.isportshop.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.isportshop.MainActivity
import com.example.isportshop.MapsActivity
import com.example.isportshop.R
import com.example.isportshop.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Profile : Fragment() {
    private lateinit var auth: FirebaseAuth
    lateinit var name : TextView
    lateinit var lastname : TextView
    lateinit var email : TextView
    lateinit var password : TextView
    lateinit var balance : TextView
    lateinit var maps : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name = view.findViewById(R.id.txtNameProfile)
        lastname = view.findViewById(R.id.txtLastnameProfile)
        email = view.findViewById(R.id.txtEmailProfile)
        password = view.findViewById(R.id.txtPasswordProfile)
        balance = view.findViewById(R.id.txtBalanceProfile)
        maps = view.findViewById(R.id.btnMaps)

        arguments?.let {
            if(it.containsKey("userProfile")){
                //Obtain info from the database
                var doc=it.getString("userProfile").toString()
                val db = Firebase.firestore
                db.collection("users").document(doc)
                    .get()
                    .addOnSuccessListener { document ->
                        var data = document?.data
                        Log.d("PROFILE", "${data.toString()}")
                        name.text = document["name"].toString()
                        lastname.text = document["lastname"].toString()
                        email.text = document["email"].toString()
                        password.text = document["password"].toString()
                        balance.text = document["balance"].toString()
                    }
                    .addOnFailureListener { e ->
                        Log.w("FIREBASE", "Error on read the document", e)
                    }
            }
        }

        maps.setOnClickListener {
            val intent=Intent(this.context, MapsActivity::class.java)
            startActivity(intent)
        }


    }
}