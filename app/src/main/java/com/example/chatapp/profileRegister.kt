package com.example.chatapp

import android.app.Activity
import android.widget.ImageView
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID
class profileRegister : AppCompatActivity() {
//    private lateinit var fireBaseAuth : FirebaseAuth
    private val SELECT_IMAGE_REQUEST = 1
    private lateinit var imageViewProfileRegister :ImageView
    private lateinit var profileImageBitmap: Bitmap
    private lateinit var profileImageUri: Uri
    private lateinit var userNameprofileRegister: String
    private lateinit var userEmail : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_register)
        val displayNameProfileRegister  = findViewById<EditText>(R.id.displayNameProfileRegister)
        val saveButtonProfileRegister = findViewById<AppCompatButton>(R.id.saveButtonProfileRegister)
        // code for image to get picked from the user
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
                sendUserData(userName, profileImageUri) // Pass the profileImageUri to the next activity
                val intent = Intent(this, Homescreen::class.java)
                startActivity(intent)
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELECT_IMAGE_REQUEST)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == SELECT_IMAGE_REQUEST) {
            val imageUri = data?.data
            if (imageUri != null) {
                val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imageViewProfileRegister.setImageBitmap(imageBitmap)
                profileImageUri = imageUri // Update the class-level variable with the selected image URI
                Toast.makeText(this, "Image SET", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Image NOT SET", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendUserData(userName: String, imageUri: Uri?) {
        UserData.getInstance().username = userName
        UserData.getInstance().imageUri = imageUri
    }

}


