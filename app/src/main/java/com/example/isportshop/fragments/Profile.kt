package com.example.isportshop.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.isportshop.MainActivity
import com.example.isportshop.MapsActivity
import com.example.isportshop.R
import com.example.isportshop.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class Profile : Fragment() {
    private lateinit var auth: FirebaseAuth
    lateinit var name : TextView
    lateinit var lastname : TextView
    lateinit var email : TextView
    lateinit var password : TextView
    lateinit var balance : TextView
    lateinit var maps : Button

    lateinit var btnAdd : ImageButton
    lateinit var btnSave : ImageButton
    lateinit var btnDelete : ImageButton
    lateinit var altitude : TextView
    lateinit var longitude : TextView
    lateinit var country :TextView
    lateinit var state : TextView
    lateinit var city : TextView
    lateinit var postalcode : TextView
    var doc = ""

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

        btnAdd = view.findViewById(R.id.btnAddLocation)
        btnSave = view.findViewById(R.id.btnSaveLocation)
        btnDelete = view.findViewById(R.id.btnDeleteLocation)
        altitude = view.findViewById(R.id.txtAddress1)
        longitude = view.findViewById(R.id.txtAddress2)

        country = view.findViewById(R.id.txtCountry)
        state = view.findViewById(R.id.txtState)
        city = view.findViewById(R.id.txtCity)
        postalcode = view.findViewById(R.id.txtPostalCode)

        btnAdd.setBackgroundColor(Color.RED)
        btnSave.setBackgroundColor(Color.GRAY)
        btnDelete.setBackgroundColor(Color.GRAY)
        btnSave.isEnabled = false
        btnDelete.isEnabled = false

        arguments?.let {
            if(it.containsKey("userProfile")){
                //Obtain info from the database
                doc=it.getString("userProfile").toString()
                val db = Firebase.firestore
                db.collection("users").document(doc)
                    .get()
                    .addOnSuccessListener { document ->
                        var data = document?.data
                        //Log.d("PROFILE", "${data.toString()}")
                        name.text = document["name"].toString()
                        lastname.text = document["lastname"].toString()
                        email.text = document["email"].toString()
                        password.text = document["password"].toString()
                        balance.text = document["balance"].toString()
                        val map: Map<String, String> = document["location"] as Map<String, String>
                        altitude.text = map["address1"]
                        longitude.text = map["address2"]
                        country.text = map["country"]
                        state.text = map["state"]
                        city.text = map["city"]
                        postalcode.text = map["postalcode"]

                    }
                    .addOnFailureListener { e ->
                        Log.w("FIREBASE", "Error on read the document", e)
                    }
            }
        }

        maps.setOnClickListener {
            val intent=Intent(this.context, MapsActivity::class.java)
            intent.putExtra("currentUser", doc)
            startActivity(intent)
        }

        btnAdd.setOnClickListener {
            it.setBackgroundColor(Color.GRAY)
            it.isEnabled = false
            btnDelete.isEnabled = true
            btnSave.isEnabled = true
            btnSave.setBackgroundColor(Color.RED)
            btnDelete.setBackgroundColor(Color.RED)
            altitude.isEnabled = true
            longitude.isEnabled = true
            country.isEnabled = true
            state.isEnabled = true
            city.isEnabled = true
            postalcode.isEnabled = true
            maps.isEnabled = false
        }

        btnSave.setOnClickListener {
            it.setBackgroundColor(Color.GRAY)
            btnDelete.setBackgroundColor(Color.GRAY)
            btnAdd.setBackgroundColor(Color.RED)
            it.isEnabled = false
            btnDelete.isEnabled = false
            btnAdd.isEnabled = true
            altitude.isEnabled = false
            longitude.isEnabled = false
            country.isEnabled = false
            state.isEnabled = false
            city.isEnabled = false
            postalcode.isEnabled = false
            maps.isEnabled = true

            val map: Map<String, String> = mapOf("address1" to altitude.text.toString(),"address2" to longitude.text.toString(),"country" to country.text.toString(), "state" to state.text.toString(),"city" to city.text.toString(),"postalcode" to postalcode.text.toString())
            val db = Firebase.firestore
            db.collection("users").document(doc)
                .update("location", map)
                .addOnSuccessListener {
                    Log.wtf("Profile address error", "Update went well")
                }
                .addOnFailureListener { e ->
                    Log.wtf("Profile address error", "Error on read the document", e)
                }

        }

        btnDelete.setOnClickListener {
            it.setBackgroundColor(Color.GRAY)
            btnSave.setBackgroundColor(Color.GRAY)
            btnAdd.setBackgroundColor(Color.RED)
            it.isEnabled = false
            btnSave.isEnabled = false
            btnAdd.isEnabled = true
            altitude.isEnabled = false
            longitude.isEnabled = false
            country.isEnabled = false
            state.isEnabled = false
            city.isEnabled = false
            postalcode.isEnabled = false
            maps.isEnabled = true

            updateDirection()

        }
    }

    private fun updateDirection(){
        val db = Firebase.firestore
        db.collection("users").document(doc)
            .get()
            .addOnSuccessListener { document ->
                var data = document?.data
                //Log.d("PROFILE", "${data.toString()}")
                name.text = document["name"].toString()
                lastname.text = document["lastname"].toString()
                email.text = document["email"].toString()
                password.text = document["password"].toString()
                balance.text = document["balance"].toString()
                val map: Map<String, String> = document["location"] as Map<String, String>
                altitude.text = map["address1"]
                longitude.text = map["address2"]
                country.text = map["country"]
                state.text = map["state"]
                city.text = map["city"]
                postalcode.text = map["postalcode"]

            }
            .addOnFailureListener { e ->
                Log.w("FIREBASE", "Error on read the document", e)
            }
    }

    override fun onResume() {
        updateDirection()
        super.onResume()

    }



}