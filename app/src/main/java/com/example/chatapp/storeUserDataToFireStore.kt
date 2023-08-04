package com.example.chatapp

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

private fun storeUserDataToFireStore(context: Context, name:String, email:String, password:String){

    val db = FirebaseFirestore.getInstance()
    val user = User(name, email, password)

    // Assuming you want to store the user information under a collection called "users"
    db.collection("users")
        .add(user) // This will generate a unique document ID for each user
        .addOnSuccessListener {
            // Data added successfully
            showToast(context,"User data stored in Firestore.")
        }
        .addOnFailureListener { e ->
            // Handle any errors
            Toast.makeText(context, "Error storing user data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}

private fun showToast(context:Context,message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}