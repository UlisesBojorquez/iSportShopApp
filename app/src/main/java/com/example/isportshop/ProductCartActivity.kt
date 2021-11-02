package com.example.isportshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class ProductCartActivity : AppCompatActivity() {

    lateinit var nameP : TextView
    lateinit var image : ImageView
    lateinit var description : TextView
    lateinit var price : TextView
    lateinit var stock : TextView
    var productName = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_product_cart)

        nameP=findViewById(R.id.productCart_name_detail)
        image=findViewById(R.id.productCart_image_detail)
        description=findViewById(R.id.productCart_description_detail)
        price=findViewById(R.id.productCart_price_detail)


        nameP.setText(intent.getStringExtra("name"))
        Picasso.get().load(intent.getStringExtra("image")).into(image)
        description.setText(intent.getStringExtra("description"))
        price.setText("Price: $" + intent.getStringExtra("price"))

        productName = intent.getStringExtra("name").toString()

    }
}