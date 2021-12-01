package com.example.isportshop

import android.app.Activity
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    //Attributes
    private lateinit var auth: FirebaseAuth
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var btnGoogle : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = Firebase.auth
        // Initialize inputs
        email = findViewById(R.id.txtEmail)
        password = findViewById(R.id.txtPassword)
        btnGoogle = findViewById(R.id.btnGoogle)

        // Google singIn button, click to begin
        btnGoogle.setOnClickListener {
            val googleConfig = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id2))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConfig)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, RC_SIGN_IN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if(account!=null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if(it.isSuccessful){
                            var googleEmail = it.result.user?.email
                            var googleCompleteName = it.result.user?.displayName?.split(" ")
                            if(googleCompleteName?.size == 1){
                                var googleName = googleCompleteName?.get(0)
                                var googleLastname = ""
                                RegisterUser(googleEmail.toString(), googleName, googleLastname)
                            }else{
                                var googleName = googleCompleteName?.get(0)
                                var googleLastname = googleCompleteName?.get(1)
                                RegisterUser(googleEmail.toString(), googleName.toString(), googleLastname.toString())
                            }
                            val intent=Intent(this,MenuActivity::class.java)
                            intent.putExtra("userInfo",googleEmail)
                            startActivity(intent)
                            Toast.makeText(this,"Login successfully",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this,"Email or password incorrect",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }catch (e:ApiException){
                Log.e("GOOGLE", e.message.toString())
            }
        }
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

    private fun RegisterUser(email: String, name: String, lastname: String){

        val db = Firebase.firestore
        db.collection("users").document(email)
            .get()
            .addOnSuccessListener { document ->
                var data = document?.data
                if(data == null){
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

                    //Add the user to the Users collection
                    val initialBalance = 5000
                    var newUser = hashMapOf(
                        "name" to name,
                        "lastname" to lastname,
                        "email" to email,
                        "password" to "",
                        "balance" to initialBalance,
                        "cartItems" to cartItemsMap,
                        "location" to location
                    )
                    db.collection("users").document(email)
                        .set(newUser)
                        .addOnSuccessListener {
                            Log.d("FIREBASE","Register went correct.")
                        }
                        .addOnFailureListener { e ->
                            Log.w("FIREBASE", "Error adding document", e)
                        }
                }
            }
            .addOnFailureListener {
                Log.e("FIREBASE", it.message.toString())
            }
    }

    companion object {
        private const val RC_SIGN_IN = 100
    }
}