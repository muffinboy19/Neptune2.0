package com.example.chatapp

import android.app.Activity
import android.widget.ImageView
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
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

//import com.google.firebase.auth.FirebaseAuth

//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.StorageReference

import java.util.UUID

class profileRegister : AppCompatActivity() {
    //    private lateinit var fireBaseAuth : FirebaseAuth
    private val SELECT_IMAGE_REQUEST = 1
    private lateinit var imageViewProfileRegister: ImageView
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var saveButtonProfileRegister: AppCompatButton
    private lateinit var loadingView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_register)
        FirebaseApp.initializeApp(this)
        loadingView = findViewById<View>(R.id.loadingView)
        firebaseStorage = FirebaseStorage.getInstance()
        val displayNameProfileRegister = findViewById<EditText>(R.id.displayNameProfileRegister)
        saveButtonProfileRegister = findViewById<AppCompatButton>(R.id.saveButtonProfileRegister)
        imageViewProfileRegister = findViewById<ImageView>(R.id.imageViewProfileRegister)
        imageViewProfileRegister.setOnClickListener {
            openImagePicker()
        }

        saveButtonProfileRegister.setOnClickListener {
            showLoadingView()
            val userName = displayNameProfileRegister.text.toString().trim()
            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else if (imageViewProfileRegister.drawable == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            } else {
                val drawable = imageViewProfileRegister.drawable
                val bitmap: Bitmap = if (drawable is BitmapDrawable) {
                    drawable.bitmap
                } else {
                    // If the drawable is a VectorDrawable, convert it to a Bitmap
                    val width = drawable.intrinsicWidth
                    val height = drawable.intrinsicHeight
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)
                    bitmap
                }
                uploadImageToFirebaseStorage(bitmap)
                MyUserData.getInstance().username = userName
                val intent = Intent(this, Homescreen::class.java)
                startActivity(intent)
            }
        }

    }

    private fun adjustImageViewSize(imageView: ImageView, bitmap: Bitmap) {
        val targetWidth = imageView.width
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val targetHeight = (targetWidth / aspectRatio).toInt()
        val layoutParams = imageView.layoutParams
        layoutParams.height = targetHeight
        imageView.layoutParams = layoutParams
        imageView.scaleType =
            ImageView.ScaleType.FIT_XY // You can also use FIT_CENTER if you prefer
    }

    private fun uploadImageToFirebaseStorage(bitmap: Bitmap) {
        val storageRef: StorageReference = firebaseStorage.reference
        val imageName = "image_${System.currentTimeMillis()}.jpg"
        val imageRef: StorageReference = storageRef.child(imageName)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val imageData: ByteArray = baos.toByteArray()
        val uploadTask = imageRef.putBytes(imageData)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val imageUrll = downloadUri.toString()
                    MyUserData.getInstance().imageUrl = imageUrll
                    UserDataLiveData.imageUrlLiveData.postValue(imageUrll)
                    hideLoadingView()
                } else {
                    Toast.makeText(this, "Image URL is null", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener { e ->
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
                val imageBitmap = getBitmapFromUri(imageUri)
                if (imageBitmap != null) {
                    imageViewProfileRegister.setImageBitmap(imageBitmap)
                    adjustImageViewSize(imageViewProfileRegister, imageBitmap)
                    Toast.makeText(this, "Image SET", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Image NOT SET", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val contentResolver: ContentResolver = contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun hideLoadingView() {
        loadingView.visibility = View.GONE
    }

    private fun showLoadingView() {
        loadingView.visibility = View.VISIBLE
    }

}



