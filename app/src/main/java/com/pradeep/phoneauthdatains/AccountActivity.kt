package com.pradeep.phoneauthdatains

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val nametext = findViewById<TextView>(R.id.name)
        val emailtext = findViewById<TextView>(R.id.email)
        val agetext = findViewById<TextView>(R.id.age)
        val citytext = findViewById<TextView>(R.id.city)

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
    }
}
