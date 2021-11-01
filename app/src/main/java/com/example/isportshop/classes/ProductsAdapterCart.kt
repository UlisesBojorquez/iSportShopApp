package com.example.isportshop.classes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.isportshop.ProductActivity
import com.example.isportshop.R
import com.squareup.picasso.Picasso

class ProductsAdapterCart(private val products : ArrayList<ProductCart>) : RecyclerView.Adapter<ProductsAdapterCart.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card_cart, parent,false)
        val holder = ViewHolder(view)
        view.setOnClickListener{
            val intent = Intent(parent.context, ProductActivity::class.java)
            intent.putExtra("name",products[holder.adapterPosition].name)
            intent.putExtra("image",products[holder.adapterPosition].image)
            intent.putExtra("description",products[holder.adapterPosition].description)
            intent.putExtra("price",products[holder.adapterPosition].price.toString())
            intent.putExtra("stock",products[holder.adapterPosition].stock.toString())
            parent.context.startActivity(intent)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ProductsAdapterCart.ViewHolder, position: Int) {
        val product = products[position]
        holder.productName.text = product.name
        Picasso.get().load(product.image).into(holder.productImage)
        holder.productPrice.text = product.price.toString()

    }

    //How many products are on this list of products
    override fun getItemCount(): Int {
        return products.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val productImage : ImageView = itemView.findViewById(R.id.product_image_cart)
        val productName : TextView = itemView.findViewById(R.id.product_name_cart)
        val productPrice : TextView = itemView.findViewById(R.id.product_price_cart)


    }
}