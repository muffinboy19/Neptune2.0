package com.example.chatapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import android.util.Log
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
        mdref  = FirebaseDatabase.getInstance().reference.child("User")
        val username = UserData.getInstance().username
        val userEmail = UserData.getInstance().userEmail
        val userId = UserData.getInstance().userId
        var myUrl :String =""
        UserDataLiveData.imageUrlLiveData.observe(this) { imageUrl ->
            if (imageUrl != null) {
                // The image URL is available, you can use it here
                myUrl = imageUrl
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

        if (username != null) {
            if (userEmail != null) {
                addUserToTheDataBase(username,userEmail,myUrl,auth.currentUser!!.uid)
            }
        }








        userList = ArrayList()
        ChatUserAdapterName = ChatUserAdapter(this, userList)
        rc = findViewById(R.id.rc)
        rc.layoutManager = LinearLayoutManager(this)
        rc.adapter = ChatUserAdapterName
        mdref = FirebaseDatabase.getInstance().getReference()


        mdref.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (posSanpshot in snapshot.children) {
                    val cu = posSanpshot.getValue(ChatUser::class.java)
                    if(auth.currentUser?.uid == cu?.uid){

                    }else{
                        userList.add(cu!!)
                    }

                }
                ChatUserAdapterName.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })




















    }

    fun addUserToTheDataBase(name: String, email: String,photoUrl:String, uid: String){

        mdref = FirebaseDatabase.getInstance().getReference()
        mdref.child("user").child(uid).setValue(User(name,email,photoUrl,uid))


    }









}