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

class MainActivity : AppCompatActivity() {

    //Attributes
    private lateinit var auth: FirebaseAuth
    lateinit var email : EditText
    lateinit var password : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = Firebase.auth
        //Initialize inputs
        email = findViewById(R.id.txtEmail)
        password = findViewById(R.id.txtPassword)

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        /*val currentUser = auth.currentUser
        if(currentUser != null){
            val intent=Intent(this,MenuActivity::class.java)
            startActivity(intent)
        }*/
    }

    private fun ValidateInputsLogin():Boolean{
        var flag = true
        if(TextUtils.isEmpty(email.text.toString())){
            flag = false
        }
        if(TextUtils.isEmpty(password.text.toString())){
            flag = false
        }
        return flag
    }

    public fun LogIn(v: View?){
        if(ValidateInputsLogin()){
            auth.signInWithEmailAndPassword(email.text.toString(),password.text.toString())
                .addOnCompleteListener(this){
                    if(it.isSuccessful){
                        Log.d("FIREBASE","Successful Login.")
                        //Send info through the intent to the menu activity
                        val intent=Intent(this,MenuActivity::class.java)
                        intent.putExtra("userInfo",email.text.toString())
                        startActivity(intent)
                    }else{
                        Log.e("FIREBASE","Login went wrong: ${it.exception?.message}.")
                        Toast.makeText(this,"Email or password incorrect",Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
            Log.w("INPUTS","Inputs missing in main.")
            Toast.makeText(this,"Email or password missing",Toast.LENGTH_SHORT).show()
        }
    }

    public fun MoveRegister(v:View?){
        val intent=Intent(this,RegisterActivity::class.java)
        startActivity(intent)
    }

    public fun RecoverPassword(v:View?){
        val intent=Intent(this,RecoverPasswordActivity::class.java)
        startActivity(intent)
    }
}