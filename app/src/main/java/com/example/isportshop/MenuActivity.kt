package com.example.isportshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.isportshop.fragments.Cart
import com.example.isportshop.fragments.Menu
import com.example.isportshop.fragments.Profile
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MenuActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var navigation : BottomNavigationView
    private val profileFragment = Profile()
    private val menuFragment = Menu()
    private val cartFragment = Cart()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        //Fragment
        replaceFragment(menuFragment)
        //Events onclick of fragments
        navigation = findViewById(R.id.bottom_Navigation)
        navigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.ic_profile -> {
                    val userData= intent.getStringExtra("userInfo")
                    Log.wtf("USERINFO", userData)
                    var bundle=Bundle()
                    bundle.putString("userProfile",userData)
                    profileFragment.arguments = bundle
                    replaceFragment(profileFragment)
                }
                R.id.ic_menu -> {
                    replaceFragment(menuFragment)
                }
                R.id.ic_cart -> {
                    val userData= intent.getStringExtra("userInfo")
                    var bundle=Bundle()
                    bundle.putString("userCart",userData)
                    cartFragment.arguments = bundle
                    replaceFragment(cartFragment)
                }
            }
            true
        }

        // Initialize Firebase Auth
        auth = Firebase.auth

    }

    private fun replaceFragment(fragment : Fragment){
        if(fragment!=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container,fragment)
            transaction.commit()
        }
    }

    fun searchMethod(view: View?){
        menuFragment.searchMethod()
    }

    public fun LogOut(v:View?){
        auth.signOut()
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}