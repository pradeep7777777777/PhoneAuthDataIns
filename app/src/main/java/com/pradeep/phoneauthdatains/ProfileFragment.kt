package com.pradeep.phoneauthdatains

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val nametext = view.findViewById<TextView>(R.id.name)
        val emailtext = view.findViewById<TextView>(R.id.email)
        val agetext = view.findViewById<TextView>(R.id.age)
        val citytext = view.findViewById<TextView>(R.id.city)

        val db = Firebase.firestore
        val userId = Firebase.auth.currentUser?.uid.toString()

        db.collection("school").document(userId).get().addOnSuccessListener {

            if (it != null) {
                val getname = it.data?.get("name")
                val getemail = it.data?.get("email")
                val getcity = it.data?.get("city")
                val getage = it.data?.get("age")

                nametext.setText("$getname")
                citytext.setText("$getcity")
                emailtext.setText("$getemail")
                agetext.setText("$getage")
            }
        }
        return view
    }
}



