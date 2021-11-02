package com.example.isportshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var confirmPassword : EditText
    lateinit var name : EditText
    lateinit var lastName : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth
        //Initialize inputs
        email = findViewById(R.id.txtEmailRegister)
        password = findViewById(R.id.txtPasswordRegister)
        confirmPassword = findViewById(R.id.txtPasswordRegister2)
        name = findViewById(R.id.txtNameRegister)
        lastName = findViewById(R.id.txtLastNameRegister)
    }

    private fun ValidateInputsRegister():Boolean{
        var flag = true

        if(TextUtils.isEmpty(email.text.toString())){
            flag = false
        }
        if(TextUtils.isEmpty(password.text.toString())){
            flag = false
        }
        if(TextUtils.isEmpty(confirmPassword.text.toString())){
            flag = false
        }
        if(TextUtils.isEmpty(name.text.toString())){
            flag = false
        }
        if(TextUtils.isEmpty(lastName.text.toString())){
            flag = false
        }

        return flag
    }

    public fun Register(v:View?){
        if(ValidateInputsRegister()){
            if(!password.text.toString().contentEquals(confirmPassword.text.toString())){
                Log.e("INPUTS","Password doesn't match.")
                Toast.makeText(this,"Incorrect password",Toast.LENGTH_SHORT).show()
                confirmPassword.setText("");
            }else{
                var cartItemsMap = mutableMapOf<String, Number>()
                var location = mutableMapOf<String, String>()
                location["altitude"]= ""
                location["longitude"]=""
                location["country"]=""
                location["state"]=""
                location["city"]=""
                location["postalcode"]=""
                location["address1"]=""
                location["address2"]=""

                auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString())
                    .addOnCompleteListener(this) {
                        if(it.isSuccessful){
                            //Add the user to the Users collection
                            val initialBalance = 5000
                            var newUser = hashMapOf(
                                "name" to name.text.toString(),
                                "lastname" to lastName.text.toString(),
                                "email" to email.text.toString(),
                                "password" to password.text.toString(),
                                "balance" to initialBalance,
                                "cartItems" to cartItemsMap,
                                "location" to location
                            )
                            val db = Firebase.firestore
                            db.collection("users").document(email.text.toString())
                                .set(newUser)
                                .addOnSuccessListener {
                                    Log.d("FIREBASE","Register went correct.")
                                    Toast.makeText(this,"User registered",Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Log.w("FIREBASE", "Error adding document", e)
                                }

                        }else{
                            Log.e("FIREBASE","Register went wrong: ${it.exception?.message}.")
                            Toast.makeText(this,"User already exist",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }else{
            Log.w("INPUTS","Inputs missing in register.")
            Toast.makeText(this,"Fill the blank inputs",Toast.LENGTH_SHORT).show()
        }

    }

    public fun MoveLogIn(v: View?){
        finish()
    }


}