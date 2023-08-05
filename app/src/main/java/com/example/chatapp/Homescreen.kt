package com.example.chatapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


class Homescreen : AppCompatActivity() {
    private val db  = FirebaseFirestore.getInstance()
    private lateinit var auth:FirebaseAuth






    private lateinit var rc: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var mdref :DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        FirebaseApp.initializeApp(this)
        val username = UserData.getInstance().username
        val userEmail = UserData.getInstance().userEmail
        val imageUri = UserData.getInstance().imageUri
        val userId = UserData.getInstance().userId










        fun uploadImageToFirebaseStorage(imageUri: Uri, onImageUploaded: (String) -> Unit) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val storageRef: StorageReference = FirebaseStorage.getInstance().reference
            val imageRef: StorageReference = storageRef.child("images/$userId/${UUID.randomUUID()}.jpg")

            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val imageUrl = downloadUri.toString()
                        onImageUploaded(imageUrl)
                    }
                }
                .addOnFailureListener { e ->
                    // Handle any errors
                }
        }
        fun saveUserDetailsToFirestore(username: String, imageUri: Uri?, userEmail: String) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

            // Upload the image to Firebase Storage and get the URL
            if (imageUri != null) {
                uploadImageToFirebaseStorage(imageUri) { imageUrl ->
                    // Once the image is uploaded, save user details in Firestore
                    val userDetails = User(username, imageUrl, userEmail)

                    val db = FirebaseFirestore.getInstance()
                    db.collection("usersZ")
                        .document(userId)
                        .set(userDetails)
                        .addOnSuccessListener {
                            // User details and image URL saved successfully
                        }
                        .addOnFailureListener { e ->
                            // Handle any errors
                        }
                }
            }
        }



















        }


}