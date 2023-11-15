package com.pradeep.phoneauthdatains

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    var verificationId =""
    private  var auth= FirebaseAuth.getInstance()
    private lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    @SuppressLint("SuspiciousIndentation")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


                val phone = findViewById<EditText>(R.id.phone)
                val otp = findViewById<EditText>(R.id.otp)
                val sendotp = findViewById<Button>(R.id.sendotp)
                val verifyotp = findViewById<Button>(R.id.verifyotp)

                sendotp.setOnClickListener {
                    sendotp(phone.text.toString())
                }
                verifyotp.setOnClickListener {
                    val credential = PhoneAuthProvider.getCredential(verificationId,otp.text.toString())
                    auth.signInWithCredential(credential)
                        .addOnSuccessListener {
                            Toast.makeText(this, "successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,SignupActivity::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                        }
                }


                callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        verifyotp(p0)
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        Toast.makeText(this@MainActivity, p0.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(p0, p1)
                        verificationId = p0
                    }
                }
            }
            private fun sendotp(phone: String) {
                val phoneAuthOptions = PhoneAuthOptions.newBuilder()
                    .setPhoneNumber("+91$phone")
                    .setTimeout(91L, TimeUnit.SECONDS)
                    .setCallbacks(callback

                    )
                    .setActivity(this)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
            }

            private fun verifyotp(credential: PhoneAuthCredential) {
                auth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        val uid = it.user?.uid
                        Toast.makeText(this@MainActivity,uid, Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@MainActivity,it.message, Toast.LENGTH_SHORT).show()
                    }
            }
        }