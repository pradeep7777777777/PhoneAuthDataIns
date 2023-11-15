package com.pradeep.phoneauthdatains

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipData.Item
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.UUID

class ViewActivity<StorageReference> : AppCompatActivity() {
        lateinit var imageView: ImageView

    companion object {
        val selecterImageCode = 100
        val CAMERA_REQUEST = 123
        var fileUri: Uri? = null;
    }

    private val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userId =auth.currentUser?.uid.toString()
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        imageView = findViewById(R.id.profileimage)
       val buttongallry = findViewById<Button>(R.id.gallry)
        val buttoncamera = findViewById<Button>(R.id.camera)
        val buttonuplod = findViewById<Button>(R.id.uploddatabase)

        val nametext = findViewById<TextView>(R.id.name)
        val emailtext = findViewById<TextView>(R.id.email)
        val agetext = findViewById<TextView>(R.id.age)
        val citytext = findViewById<TextView>(R.id.city)
        val update = findViewById<ImageView>(R.id.update)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.tools)

        buttongallry.setOnClickListener {
            openGallery(imageView)
        }
        buttoncamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST)
        }
        buttonuplod.setOnClickListener {
            uploadImage()
        }


        toolbar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.logout1->{
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnMenuItemClickListener true
                }
                R.id.Call ->{

                    val intent = Intent(this,CallActivity::class.java)
                    startActivity(intent)
                    return@setOnMenuItemClickListener true
                }
                R.id.Delete ->{
                    db.collection("school").document(userId).delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Delete Successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Not Delete", Toast.LENGTH_SHORT).show()
                        }
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                    return@setOnMenuItemClickListener true
                }

                R.id.Email ->{
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.data = Uri.parse("Email")
                        val str_array = arrayOf("pkhaker77@gmail.com","kumarpramod80592@gmail")
                        intent.putExtra(Intent.EXTRA_EMAIL,str_array)
                        intent.putExtra(Intent.EXTRA_SUBJECT,"this field is for subject")
                        intent.putExtra(Intent.EXTRA_TEXT,"this field is for Email Body")
                        intent.type = "message/rfc822"
                        val a = Intent.createChooser(intent,"https://maps.app.goo.gl/LoHhwn2a6tjsMTfE9")
                        startActivity(a)

                    return@setOnMenuItemClickListener true
                }
                R.id.Account ->  {
                    val intent = Intent(this,AccountActivity::class.java)
                    startActivity(intent)
                    return@setOnMenuItemClickListener true
                }

                R.id.Help ->  {
                    Toast.makeText(this, "Help_amp_support", Toast.LENGTH_SHORT).show()
                    return@setOnMenuItemClickListener true
                }

                R.id.Product ->  {
                    val intent = Intent(this,ProductActivity::class.java)
                    startActivity(intent)
                    return@setOnMenuItemClickListener true
                }

                else -> {
                    return@setOnMenuItemClickListener true
                }
            }
      }

        update.setOnClickListener {
            val intent = Intent(this,UpdateActivity::class.java)
            startActivity(intent)
        }

        val db = Firebase.firestore
        val userId = Firebase.auth.currentUser?.uid.toString()

        db.collection("school").document(userId).get().addOnSuccessListener {

            if(it != null){
                val getname=it.data?.get("name")
                val getemail=it.data?.get("email")
                val getcity=it.data?.get("city")
                val getage=it.data?.get("age")

                nametext.setText("$getname")
                citytext.setText("$getcity")
                emailtext.setText("$getemail")
                agetext.setText("$getage")
            }
        }
    }
    // functon

        fun openGallery(v: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture.."), selecterImageCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == selecterImageCode && resultCode == Activity.RESULT_OK) { // Change this line
            if (data != null) {
                try {
                    fileUri = data.data
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
                    imageView.setImageBitmap(bitmap) // Change this line
                } catch (exp: IOException) {
                    exp.printStackTrace()
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            val ppp = data!!.extras?.get("data") as Bitmap
            imageView.setImageBitmap(ppp)
        }
    }
    fun uploadImage() {
        // on below line checking weather our file uri is null or not.
        if (fileUri != null) {
            // on below line displaying a progress dialog when uploading an image.
            val progressDialog = ProgressDialog(this)
            // on below line setting title and message for our progress dialog and displaying our progress dialog.
            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Uploading your image..")
            progressDialog.show()

            // on below line creating a storage refrence for firebase storage and creating a child in it with
            // random uuid.
            val ref: com.google.firebase.storage.StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())
            // on below line adding a file to our storage.
            ref.putFile(fileUri!!).addOnSuccessListener {
                // this method is called when file is uploaded.
                // in this case we are dismissing our progress dialog and displaying a toast message
                progressDialog.dismiss()
                Toast.makeText(this, "Image Uploaded..", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                // this method is called when there is failure in file upload.
                // in this case we are dismissing the dialog and displaying toast message
                progressDialog.dismiss()
                Toast.makeText(this, "Fail to Upload Image..", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}