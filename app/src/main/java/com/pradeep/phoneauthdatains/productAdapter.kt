package com.pradeep.phoneauthdatains

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class productAdapter(val productDetail: List<productModel>, val context: Context):RecyclerView.Adapter<productAdapter.Myrecyclerview>() {
    class Myrecyclerview(itemView: View):RecyclerView.ViewHolder(itemView){
       val title = itemView.findViewById<TextView>(R.id.titlename)
        val price = itemView.findViewById<TextView>(R.id.price)
        val description = itemView.findViewById<TextView>(R.id.description)
        val images = itemView.findViewById<ImageView>(R.id.image)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myrecyclerview {
    val layout = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_list,parent,false)
        return Myrecyclerview(layout)
    }

    override fun getItemCount(): Int {
        return productDetail.size
    }

    override fun onBindViewHolder(holder: Myrecyclerview, position: Int) {
        holder.title.text = "Product:- "+ productDetail[position].title
        holder.price.text = "Price:- "+ productDetail[position].price.toString()
        holder.description.text = "Description:- "+ productDetail[position].description
        Glide.with(context).load(productDetail[position].image).into(holder.images)
    }

}

