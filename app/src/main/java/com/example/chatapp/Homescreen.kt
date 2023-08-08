package com.example.chatapp


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
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


class Homescreen : AppCompatActivity(),ChatUserAdapter.OnUserClickListener {
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



        mdref = FirebaseDatabase.getInstance().getReference("users")

        UserDataLiveData.imageUrlLiveData.observe(this) { imageUrl ->
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).into(profileIcon)
                val userId = UserData.getInstance().userId
                val username = UserData.getInstance().username
                val userEmail = UserData.getInstance().userEmail

                val user = hashMapOf(
                    "userId" to userId,
                    "username" to username,
                    "userEmail" to userEmail,
                    "userImageUrl" to imageUrl
                )

                if (userId != null) {
                    // Use Realtime Database to store the data
                    mdref.child(userId).setValue(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Added to Realtime Database", Toast.LENGTH_SHORT).show()
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
        ChatUserAdapterName = ChatUserAdapter(this, userList,this)
        rc = findViewById(R.id.rc)
        rc.layoutManager = LinearLayoutManager(this)
        rc.adapter = ChatUserAdapterName

        mdref = FirebaseDatabase.getInstance().getReference("users")
        mdref.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUserUid = auth.currentUser?.uid
                for (userSnapshot in snapshot.children) {
                    val cu = userSnapshot.getValue(ChatUser::class.java)
                    if (cu != null && currentUserUid != cu.uid && currentUserUid != userSnapshot.key) {
                        userList.add(cu)
                    }
                }
                ChatUserAdapterName.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Listen failed.", error.toException())
            }
        })



    }

     private  fun addUserToTheDataBase(name: String, email: String, photoUrl: String, uid: String) {
        mdref = FirebaseDatabase.getInstance().getReference("users")
        mdref.child(uid).setValue(User(name, email, photoUrl, uid))
    }

    override fun onUserClick(uid: String) {
        val intent = Intent(this, ChatScreen::class.java)
        intent.putExtra("uid", uid) // Pass the UID to the ChatScreen activity
    }


}