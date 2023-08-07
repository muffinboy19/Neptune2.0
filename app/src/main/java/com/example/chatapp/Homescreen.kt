package com.example.chatapp


import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase


class Homescreen : AppCompatActivity() {
    private val db  = FirebaseFirestore.getInstance()
    private lateinit var auth:FirebaseAuth
    private lateinit var mdref :DatabaseReference
    private lateinit var rc: RecyclerView
    private lateinit var userList: ArrayList<ChatUser>
    private lateinit var ChatUserAdapterName :ChatUserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        val profileIcon = findViewById<ImageView>(R.id.profileIcon)

        profileIcon.setOnClickListener {
            Toast.makeText(this,"You are cute",Toast.LENGTH_SHORT).show()
        }



       // mdref  = FirebaseDatabase.getInstance().reference.child("User")


        val username = UserData.getInstance().username
        val userEmail = UserData.getInstance().userEmail
        val userId = UserData.getInstance().userId
        var myUrl: String = ""
        UserDataLiveData.imageUrlLiveData.observe(this) { imageUrl ->
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).into(profileIcon)
                myUrl = imageUrl
                val user = hashMapOf(
                    "userId" to userId,
                    "username" to username,
                    "userEmail" to userEmail,
                    "userImageUrl" to imageUrl
                )

                if (userId != null) {
                    // Use Firestore to store the data
                    db.collection("users").document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Added to Firestore", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Upload failed bro", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                // The image URL is null, handle the case if needed
                Toast.makeText(this, "Image URL is null", Toast.LENGTH_SHORT).show()
            }
        }

        userList = ArrayList()
        ChatUserAdapterName = ChatUserAdapter(this, userList)
        rc = findViewById(R.id.rc)
        rc.layoutManager = LinearLayoutManager(this)
        rc.adapter = ChatUserAdapterName


        mdref = FirebaseDatabase.getInstance().getReference()

        db.collection("users").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            userList.clear()
            if (snapshot != null) {
                for (document in snapshot) {
                    val cu = document.toObject(ChatUser::class.java)
                    if (auth.currentUser?.uid != cu.uid) {
                        userList.add(cu)
                    } else {

                    }
                }
            }
            ChatUserAdapterName.notifyDataSetChanged()
        }




















    }

    fun addUserToTheDataBase(name: String, email: String, photoUrl: String, uid: String) {
        mdref = FirebaseDatabase.getInstance().getReference("User") // Use "User" as the node name
        mdref.child(uid).setValue(User(name, email, photoUrl, uid))
    }










}