package com.pradeep.phoneauthdatains

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()
    lateinit var arrayList: ArrayList<productModel>
    lateinit var productAdapter:productAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         val view= inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        db.collection("product").get()
            .addOnSuccessListener {
                val result = it.toObjects(productModel::class.java)
                arrayList = ArrayList()
                arrayList.addAll(result)
                recyclerView.adapter = productAdapter(arrayList,requireActivity())
            }
            .addOnSuccessListener {

            }
        return view
    }
}