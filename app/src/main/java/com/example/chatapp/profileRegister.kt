package com.example.chatapp

import android.widget.ImageView
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
class profileRegister : AppCompatActivity() {

    private val SELECT_IMAGE_REQUEST = 1
    private lateinit var imageViewProfileRegister :ImageView
    private lateinit var profileImageBitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_register)
        val editTextViewProfieRegister = findViewById<EditText>(R.id.editTextViewProfieRegister)
        val saveButtonProfileRegister = findViewById<Button>(R.id.saveButtonProfileRegister)

        // code for image to get picked from the user
        imageViewProfileRegister = findViewById<ImageView>(R.id.imageViewProfileRegister)
        imageViewProfileRegister.setOnClickListener {
            openImagePicker()
        }
        saveButtonProfileRegister.setOnClickListener{
            val userName = editTextViewProfieRegister.text.toString().trim()
            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else if (!::profileImageBitmap.isInitialized) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            } else {
                // Save the image and user name in SharedPreferences
                saveDataToSharedPreferences(userName, profileImageBitmap)
                val intent = Intent(this,Homescreen::class.java)
                startActivity(intent)

                // Do any additional processing or navigate to the next screen as needed.
            }
        }

    }

    private fun saveDataToSharedPreferences(userName: String, profileImageBitmap: Bitmap) {
        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Save user name
        editor.putString("UserName", userName)

        // Save image as a Base64 encoded string
        val byteArrayOutputStream = ByteArrayOutputStream()
        profileImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
        editor.putString("UserProfileImage", encodedImage)
        editor.apply()
    }


    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELECT_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE_REQUEST) {
            val imageUri = data?.data
            if (imageUri != null) {
                val bitmap = decodeImageUri(imageUri)
                if (bitmap != null) {
                    profileImageBitmap = bitmap
                    // Update the ImageView to show the selected profile image
                    val imageView: ImageView = findViewById(R.id.imageViewProfileRegister)
                    imageView.setImageBitmap(bitmap)
                } else {
                    // Handle image decoding error
                }
            }
        }
    }

    private fun decodeImageUri(imageUri: Uri): Bitmap? {
        val inputStream = contentResolver.openInputStream(imageUri)
        return BitmapFactory.decodeStream(inputStream)
    }

    // Now, you can save the profileImageBitmap to use it further as per your requirement.
    // You can use any method of your choice to save it (e.g., in SharedPreferences, local database, etc.).



}


