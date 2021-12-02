package com.example.isportshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class ProductShopHistoryActivity : AppCompatActivity() {

    lateinit var date : TextView
    lateinit var totalAmount : TextView
    var itemsList = arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_product_shop_history)

        date=findViewById(R.id.tvProductShopDate)
        totalAmount=findViewById(R.id.tvProductShopPrice)

        date.setText(intent.getStringExtra("date"))
        //Picasso.get().load(intent.getStringExtra("image")).into(image)
        totalAmount.setText("Total amount: $" + intent.getStringExtra("price"))


    }
}