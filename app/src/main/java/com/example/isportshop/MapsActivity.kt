package com.example.isportshop

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.isportshop.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    var fusedLocationProviderClient : FusedLocationProviderClient? = null
    var currentLocation : Location? = null
    var currentMarker : Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)*/
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latLong = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
        drawMarker(latLong)

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(p0: Marker) {
                if(currentMarker !=null)
                    currentMarker?.remove()

                val newLatLng = LatLng(p0?.position!!.latitude, p0?.position!!.longitude)
                drawMarker(newLatLng)

                var address = getAddress(newLatLng.latitude, newLatLng.longitude).toString()
                Log.d("DIRECCION", address[0].toString())
                var addressDirections = address.split(",")
                var addressInternal = addressDirections[2].trimStart().trimEnd().split(" ")

                var doc=intent.getStringExtra("currentUser").toString()
                val map: Map<String, String> = mapOf("altitude" to newLatLng.latitude.toString(),"longitude" to newLatLng.longitude.toString(),"address1" to addressDirections[0],"country" to addressDirections[3], "state" to addressInternal[0],"city" to addressDirections[1],"postalcode" to addressInternal[1])
                val db = Firebase.firestore
                db.collection("users").document(doc)
                    .update("location", map)
                    .addOnSuccessListener { document ->
                        Log.d("PROFILE", "Update went well")
                    }
                    .addOnFailureListener { e ->
                        Log.wtf("PROFILE", "Error on read the document", e)
                    }
            }

            override fun onMarkerDragStart(p0: Marker) {

            }
        })
    }

    private fun drawMarker(latLong : LatLng){
        val markerOption = MarkerOptions().position(latLong).title(latLong.toString())
            .snippet(getAddress(latLong.latitude, latLong.longitude)).draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLong))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15f),4000, null)
        currentMarker = mMap.addMarker(markerOption)
        currentMarker?.showInfoWindow()
    }

    private fun getAddress(latitude: Double, longitude: Double): String? {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(latitude, longitude, 1)
        return address[0].getAddressLine(0).toString()
    }

    private fun fetchLocation() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1000)
            return
        }
        val lastLocation = fusedLocationProviderClient?.lastLocation
        lastLocation?.addOnSuccessListener { location ->
            if(location != null){
                /*var doc=intent.getStringExtra("currentUser").toString()
                val db = Firebase.firestore
                Log.wtf("afuera", "afuera")
                db.collection("users").document(doc)
                    .get()
                    .addOnSuccessListener { document ->
                        var data = document?.data
                        Log.d("PROFILE", "${data.toString()}")
                        val map: Map<String, String> = document["location"] as Map<String, String>
                        var altitude= map["altitude"]
                        var longitude = map["longitude"]
                        if(!TextUtils.isEmpty(altitude) || !TextUtils.isEmpty(longitude)){
                            val latLong = LatLng(altitude.toString().toDouble(), longitude.toString().toDouble())
                            drawMarker(latLong)
                            location.latitude = altitude.toString().toDouble()
                            location.longitude = longitude.toString().toDouble()
                            Log.wtf("ENTRE", "ENTRE")
                        }

                    }
                    .addOnFailureListener { e ->
                        Log.w("MAPS ACTIVITY", "Error on read the document", e)
                    }*/
                this.currentLocation = location
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000 -> if(grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                fetchLocation()
        }
    }

}