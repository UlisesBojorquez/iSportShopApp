package com.example.isportshop.classes

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.isportshop.ProductCartActivity
import com.example.isportshop.ProductShopHistoryActivity
import com.example.isportshop.R
import com.squareup.picasso.Picasso

class ShopHistoryAdapter(private val products : ArrayList<Shop>) : RecyclerView.Adapter<ShopHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shop_card, parent,false)
        val holder = ViewHolder(view)

        view.setOnClickListener{
            val intent = Intent(parent.context, ProductShopHistoryActivity::class.java)
            //Log.e("FIRESTORE", "error: $e")
            intent.putExtra("date",products[holder.adapterPosition].date)
            //intent.putExtra("image",products[holder.adapterPosition].image)
            intent.putExtra("totalAmount",products[holder.adapterPosition].totalAmount.toString())
            parent.context.startActivity(intent)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ShopHistoryAdapter.ViewHolder, position: Int) {
        val product = products[position]
        holder.shopDate.text = product.date
        //Picasso.get().load(product.image).into(holder.shopImage)
        holder.shopTotalAmount.text = product.totalAmount.toString()
    }

    //How many products are on this list of products
    override fun getItemCount(): Int {
        return products.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //val shopImage : ImageView = itemView.findViewById(R.id.iv_image_shop)
        val shopDate : TextView = itemView.findViewById(R.id.tv_shop_date)
        val shopTotalAmount : TextView = itemView.findViewById(R.id.tv_total_amount)
    }
}