package com.pradeep.phoneauthdatains

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

                val userId =auth.currentUser?.uid.toString()

                val editname = findViewById<EditText>(R.id.updatename)
                val editage = findViewById<EditText>(R.id.updateage)
                val editemail = findViewById<EditText>(R.id.updateemail)
                val editaddress = findViewById<EditText>(R.id.updatecity)
                val btn =findViewById<Button>(R.id.update)
                val db = Firebase.firestore

                val res = db.collection("school").document(userId)
                res.get().addOnSuccessListener {
                    if (it != null) {

                        val name1 = it.data?.get("name")?.toString()
                        val age1 = it.data?.get("age")?.toString()
                        val email1 = it.data?.get("email")?.toString()
                        val address1 = it.data?.get("city")?.toString()


                        editname?.setText(name1)
                        editage?.setText(age1)
                        editemail?.setText(email1)
                        editaddress?.setText(address1)
                    }
                }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()

                    }
                btn.setOnClickListener {
                    val dataClass=User_Model(editname.text.toString(),editemail.text.toString()
                        ,editage.text.toString().toLong(),editaddress.text.toString(),userId)
                    db.collection("school").document(userId).set(dataClass)
                        .addOnSuccessListener {
                            Toast.makeText(this, "uptade success", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,ViewActivity::class.java))
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }