package com.example.isportshop.classes

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.isportshop.ProductActivity
import com.example.isportshop.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProductsAdapter(private val products : ArrayList<Product>) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card, parent,false)
        val holder = ViewHolder(view)
        holder.productImage.setOnClickListener{
            val intent = Intent(parent.context, ProductActivity::class.java)
            intent.putExtra("id",products[holder.adapterPosition].id)
            intent.putExtra("name",products[holder.adapterPosition].name)
            intent.putExtra("image",products[holder.adapterPosition].image)
            intent.putExtra("description",products[holder.adapterPosition].description)
            intent.putExtra("price",products[holder.adapterPosition].price.toString())
            intent.putExtra("stock",products[holder.adapterPosition].stock.toString())

            parent.context.startActivity(intent)
        }

        holder.btnAddToCart.setOnClickListener{

            AlertDialog.Builder(view.context)
                .setMessage("Added to your wish list")
                .setPositiveButton("OK") { p0, p1 ->
                }
                .create()
                .show()

            val user = Firebase.auth.currentUser
            user?.let {
                for(provider in it.providerData){
                    // Id of the provider (ex: google.com)
                    val providerId = provider.providerId

                    // UID specific to the provider
                    val uid = provider.uid

                    val email = provider.email

                    var listProduct = mutableMapOf<String, Number>()

                    Firebase.firestore.collection("users").get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                if(document["email"].toString().equals(email)) {
                                    listProduct = document["cartItems"] as MutableMap<String, Number>
                                    if(listProduct.containsKey(products[holder.adapterPosition].name)){
                                        var productAmount : Number = listProduct.getValue(products[holder.adapterPosition].name)
                                        var productAmountInt : Int = productAmount.toInt()
                                        productAmountInt++
                                        productAmount = productAmountInt
                                        listProduct.put(products[holder.adapterPosition].name, productAmount)
                                    }else{
                                        var productAmount : Number = 1
                                        listProduct.put(products[holder.adapterPosition].name, productAmount)
                                    }
                                }
                            }
                            val stringEmail = email.toString()
                            val cartItemsRef = Firebase.firestore.collection("users").document(stringEmail)

                            // Set the "isCapital" field of the city 'DC'
                            cartItemsRef
                                .update("cartItems", listProduct)
                                .addOnSuccessListener { Log.d("Res", "DocumentSnapshot successfully updated!") }
                                .addOnFailureListener { e -> Log.w("Res", "Error updating document", e) }
                        }
                }
            }

        }

        return holder
    }


    override fun onBindViewHolder(holder: ProductsAdapter.ViewHolder, position: Int) {
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
        val btnAddToCart: Button = itemView.findViewById(R.id.btnAddToCart)
        val productImage : ImageView = itemView.findViewById(R.id.product_image)
        val productName : TextView = itemView.findViewById(R.id.product_name)
        val productPrice : TextView = itemView.findViewById(R.id.product_price)


    }
}