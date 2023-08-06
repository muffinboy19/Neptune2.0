package com.example.chatapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


class Homescreen : AppCompatActivity() {
    private val db  = FirebaseFirestore.getInstance()
    private lateinit var auth:FirebaseAuth
    private lateinit var mdref :DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        mdref  = FirebaseDatabase.getInstance().reference.child("User")
        val username = UserData.getInstance().username
        val userEmail = UserData.getInstance().userEmail
        val userId = UserData.getInstance().userId
        UserDataLiveData.imageUrlLiveData.observe(this) { imageUrl ->
            if (imageUrl != null) {
                // The image URL is available, you can use it here
                val user = hashMapOf(
                    "userId" to userId,
                    "username" to username,
                    "userEmail" to userEmail,
                    "userImageUrl" to imageUrl
                )

                if (userId != null) {
                    db.collection("users").document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            Log.d("Homescreen", "User data added to Firestore.")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Homescreen", "Error adding user data to Firestore: $e")
                        }
                }
            } else {
                // The image URL is null, handle the case if needed
                Toast.makeText(this, "Image URL is null", Toast.LENGTH_SHORT).show()
            }
        }



























    }








}