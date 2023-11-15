package com.pradeep.phoneauthdatains

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat

class CallActivity : AppCompatActivity() {

    private var callbtn : Button? = null
    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)

                editText = findViewById(R.id.phone)
                callbtn = findViewById(R.id.CallBut)
            }
            fun callfun(v : View) {
                val intent = Intent(Intent.ACTION_CALL)
                val num = editText!!.text.toString()
                if (TextUtils.isEmpty(editText!!.text.toString())) {
                    editText!!.error = "Enter your no"
                    return
                } else {
                    intent.data = Uri.parse("tel:$num")
                }
                if (ActivityCompat.checkSelfPermission(
                        this@CallActivity,
                        android.Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this@CallActivity, "permission call", Toast.LENGTH_SHORT).show()
                    requestPermissions()
                } else {
                    startActivity(intent)
                }
            }
//        val button = findViewById<Button>(R.id.button)
//        val edittext = findViewById<EditText>(R.id.editText)
//
//        button.setOnClickListener {
//            val phone_number = edittext.text.toString()
//            val phone_intent = Intent(Intent.ACTION_DIAL)
//            phone_intent.data = Uri.parse("tel:$phone_number")
//
//            startActivity(phone_intent)
//        }


            private fun requestPermissions() {
                ActivityCompat.requestPermissions(this@CallActivity, arrayOf(android.Manifest.permission.CALL_PHONE),1)
            }
        }
