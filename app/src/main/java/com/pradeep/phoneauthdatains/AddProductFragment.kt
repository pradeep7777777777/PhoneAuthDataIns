package com.pradeep.phoneauthdatains

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.UUID

class AddProductFragment : Fragment() {

    private var auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    @SuppressLint("MissingInflatedId")

    lateinit var imageView: ImageView
    companion object {
        val selecterImageCode = 100
        val CAMERA_REQUEST = 123
        var fileUri: Uri? = null;
        var downloadUri: Uri? = null;
    }

    lateinit var EditTexttitle: EditText
    lateinit var EditTextprice: EditText
    lateinit var EditTextdescription: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_product, container, false)


        EditTexttitle = view.findViewById(R.id.producttittle)
        EditTextprice = view.findViewById(R.id.productprice)
        EditTextdescription = view.findViewById(R.id.productdescription)

        imageView = view.findViewById(R.id.productimage)
        val productgallery = view.findViewById<Button>(R.id.productgallery)
        val productcamera = view.findViewById<Button>(R.id.productcamera)

        val productinsertbutton = view.findViewById<Button>(R.id.productinsert)


        // images
        productgallery.setOnClickListener {
            openGallery(imageView)
        }
        productcamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,CAMERA_REQUEST)
        }
        productinsertbutton.setOnClickListener {
            uploadImage()
        }
       //
        return view
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
        if (requestCode == selecterImageCode && resultCode == Activity.RESULT_OK) {
            // Change this line
            if (data != null) {
                try {
                    fileUri = data.data
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, fileUri)
                    imageView.setImageBitmap(bitmap) // Change this line
                } catch (exp: IOException) {
                    exp.printStackTrace()
                }
            }
        }
        else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(requireActivity(), "Canceled", Toast.LENGTH_SHORT).show()
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            val ppp = data!!.extras?.get("data") as Bitmap
            imageView.setImageBitmap(ppp)
        }

        if (requestCode == CAMERA_REQUEST){
            fileUri = getImageUri(requireContext(), data?.extras?.get("data") as Bitmap)
            imageView.setImageURI(fileUri)
        }

    }

    private fun getImageUri(inContext: Context, inImage:Bitmap):Uri?{
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG,100,bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,inImage,"Title",null
        )
        return Uri.parse(path)
    }

    fun uploadImage() {
            val progressDialog = ProgressDialog(requireActivity())
            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Uploading your image..")
            progressDialog.show()
            // data insert
            val tittleIdAuth = auth.currentUser?.uid.toString()
            Toast.makeText(requireActivity(), "$tittleIdAuth", Toast.LENGTH_SHORT).show()
             //
            val ref: com.google.firebase.storage.StorageReference = FirebaseStorage.getInstance().reference
                .child(UUID.randomUUID().toString())
            ref.putFile(fileUri!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {uri ->
                    downloadUri=uri

                    val productModel = productModel(
                        EditTexttitle.text.toString(),
                        EditTextprice.text.toString().toLong(),
                        EditTextdescription.text.toString(),
                        downloadUri.toString(),
                        tittleIdAuth
                    )
                    db.collection("product").document("$tittleIdAuth").set(productModel)
                        .addOnSuccessListener {
                            Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireActivity(), "Faild", Toast.LENGTH_SHORT).show()
                        }
                }
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), "Image Uploaded..", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), "Fail to Upload Image..", Toast.LENGTH_SHORT)
                    .show()
        }
    }
}