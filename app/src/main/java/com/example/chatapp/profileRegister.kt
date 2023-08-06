package com.example.chatapp

import android.app.Activity
import android.widget.ImageView
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.common.base.MoreObjects.ToStringHelper
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.lang.RuntimeException
import java.util.UUID
class profileRegister : AppCompatActivity() {
//    private lateinit var fireBaseAuth : FirebaseAuth
    private val SELECT_IMAGE_REQUEST = 1
    private lateinit var imageViewProfileRegister :ImageView
    private lateinit var userNameprofileRegister: String



    private lateinit var imageUriProfileRegister : Uri
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var saveButtonProfileRegister : AppCompatButton
    private   var PICK_IMAGE_REQUEST_CODE = 101
    private val storageReference: StorageReference by lazy {
        FirebaseStorage.getInstance().reference.child("images")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_register)
        FirebaseApp.initializeApp(this)
        firebaseStorage = FirebaseStorage.getInstance()
        val displayNameProfileRegister  = findViewById<EditText>(R.id.displayNameProfileRegister)
        saveButtonProfileRegister = findViewById<AppCompatButton>(R.id.saveButtonProfileRegister)
        imageViewProfileRegister = findViewById<ImageView>(R.id.imageViewProfileRegister)
        imageViewProfileRegister.setOnClickListener {
            openImagePicker()
        }
        val imageViewProfileRegister: ImageView = findViewById(R.id.imageViewProfileRegister)
        saveButtonProfileRegister.setOnClickListener {
            val userName = displayNameProfileRegister.text.toString().trim()
            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else if (imageViewProfileRegister.drawable == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            } else {

                val drawable = imageViewProfileRegister.drawable as BitmapDrawable
                val bitmap = drawable.bitmap
                uploadImageToFirebaseStorage(bitmap)
                UserData.getInstance().username = userName
                val intent = Intent(this, Homescreen::class.java)
                startActivity(intent)
            }
        }
    }

    private fun uploadImageToFirebaseStorage(bitmap: Bitmap) {

        val storageRef: StorageReference = firebaseStorage.reference

        // Create a reference to the image file with a unique name (e.g., using timestamp)
        val imageName = "image_${System.currentTimeMillis()}.jpg"
        val imageRef: StorageReference = storageRef.child(imageName)

        // Convert the Bitmap to bytes (PNG format recommended for lossless quality)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val imageData: ByteArray = baos.toByteArray()

        // Upload the image data to Firebase Storage
        val uploadTask = imageRef.putBytes(imageData)

        // Handle the upload success and failure events
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully, get the download URL
            imageRef.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Get the image URL and store it in a variable
                    val downloadUri = task.result
                    val imageUrl = downloadUri.toString()
                    UserData.getInstance().imageUrl  = imageUrl
                }
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this,"Iamge upload error ",Toast.LENGTH_SHORT).show()
        }
        // Add this code in the uploadImageToFirebaseStorage function
        uploadTask.addOnFailureListener { e ->
            Toast.makeText(this, "Image Upload Failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }

    }


    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELECT_IMAGE_REQUEST)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == SELECT_IMAGE_REQUEST) {
            val imageUri: Uri? = data?.data
            if (imageUri != null) {
                val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imageViewProfileRegister.setImageBitmap(imageBitmap)
                Toast.makeText(this, "Image SET", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Image NOT SET", Toast.LENGTH_SHORT).show()
            }
        }
    }






}



