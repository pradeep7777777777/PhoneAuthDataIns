package com.pradeep.phoneauthdatains

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {
    var auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

            val name = findViewById<EditText>(R.id.name)
            val age = findViewById<EditText>(R.id.age)
            val email = findViewById<EditText>(R.id.email)
            val city = findViewById<EditText>(R.id.city)
            val button = findViewById<Button>(R.id.register)
            button.setOnClickListener() {
                val userIdAuth = auth.currentUser?.uid.toString()

                Toast.makeText(this, "$userIdAuth", Toast.LENGTH_SHORT).show()

                val userModel = User_Model(
                    name.text.toString(),
                    email.text.toString(),
                    age.text.toString().toLong(),
                    city.text.toString(),
                    userIdAuth
                )
                db.collection("school").document("$userIdAuth").set(userModel)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,ViewActivity::class.java))
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Faild", Toast.LENGTH_SHORT).show()
                    }

            }
        }
    }