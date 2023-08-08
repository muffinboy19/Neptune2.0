//package com.example.chatapp
//
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.os.Message
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import android.widget.Toolbar
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.ktx.getValue
//import com.google.firebase.ktx.Firebase
//import com.squareup.picasso.Picasso
//import java.security.MessageDigest
//
//
//class ChatScreen : AppCompatActivity() {
//    private lateinit var chatRecyclerView: RecyclerView
//    private lateinit var messageBox: EditText
//    private lateinit var sendButton: ImageView
//    private lateinit var messageAdapter: messageAdapter
//    private lateinit var messageList: ArrayList<messaage>
//    private lateinit var mdbref: DatabaseReference
//    private lateinit var mAuth: FirebaseAuth
//    private lateinit var userList : ChatUserAdapter
//
//    var recciverRoom: String? = null
//    var senderRoom: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_chatscreen)
//        mdbref = FirebaseDatabase.getInstance().getReference()
//        val imageURL  = intent.getStringExtra("imageUrl")
//        val name = intent.getStringExtra("name")
//        val Recciveruid :String? = intent.getStringExtra("uid")
//        val senderUId :String? = FirebaseAuth.getInstance().currentUser?.uid
//        mdbref = FirebaseDatabase.getInstance().getReference()
//
//
//        val UserNameDisplayChatScreen = findViewById<TextView>(R.id.UserNameDisplayChatScreen)
//        val displaydp = findViewById<ImageView>(R.id.displaydp)
//        val prev = findViewById<ImageView>(R.id.prev)
//        UserNameDisplayChatScreen.text = name
//        Picasso.get().load(imageURL).into(displaydp)
//        prev.setOnClickListener {
//            val intent = Intent(this, Homescreen::class.java)
//            startActivity(intent)
//        }
//
//
//
//        if (senderUId != null && recciverRoom != null) {
//            senderRoom = senderUId + recciverRoom
//            recciverRoom = recciverRoom + senderUId
//
//            // Rest of your code ...
//
//            mdbref.child("chats").child(recciverRoom!!).child("messages")
//                .addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        val newMessages = ArrayList<messaage>()
//                        for (postSnap in snapshot.children) {
//                            val boxo = postSnap.getValue(messaage::class.java)
//                            newMessages.add(boxo!!)
//                        }
//                        messageList.addAll(newMessages)
//                        messageAdapter.notifyDataSetChanged()
//                    }
//                    override fun onCancelled(error: DatabaseError) {
//
//                    }
//                })
//
//            // Rest of your code ...
//
//            sendButton.setOnClickListener {
//                val message = messageBox.text.toString()
//                if (!message.isNullOrEmpty()) {
//                    val messageObject = messaage(message, senderUId)
//
//                    // Push the message to sender's room
//                    mdbref.child("chats").child(senderRoom!!).child("messages").push()
//                        .setValue(messageObject)
//                        .addOnSuccessListener {
//                            // Handle success
//                        }
//
//                    // Push the same message to receiver's room
//                    mdbref.child("chats").child(recciverRoom!!).child("messages").push()
//                        .setValue(messageObject)
//                        .addOnSuccessListener {
//                            // Handle success
//                        }
//
//                    messageBox.setText("")
//                } else {
//                    // Handle empty message
//                }
//            }
//        } else {
//            // Handle the case where either senderUId or recciverRoom is null
//
//            if (senderUId == null && recciverRoom == null) {
//                Toast.makeText(this, "Sender and Receiver information are missing.", Toast.LENGTH_SHORT).show()
//            } else if (senderUId == null) {
//                Toast.makeText(this, "Sender information is missing.", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Receiver information is missing.", Toast.LENGTH_SHORT).show()
//            }
//
//            // Optionally, you might want to finish the activity or take appropriate action.
//        }
//    }
//
//
//
//        // Inside your onCreate method in ChatScreen activity
//// ... (Previous code)
//
//// Set up the listener for the receiver's chat room
//
//
//// ... (Rest of your code)
//
//
//
//
//
////        sendButton.setOnClickListener {
////
////
////            val message = messageBox.text.toString()
////            val messageObject = messaage(message,senderUId)
////
////
////            mdbref.child("chats").child(senderRoom!!).child("messages").push()
////                .setValue(messageObject)
////                .addOnSuccessListener {
////
////                }
////
////            mdbref.child("chats").child(recciverRoom!!).child("messages").push()
////                .setValue(messageObject)
////                .addOnSuccessListener {
////
////                }
////
////
////
////            messageBox.setText("")
////        }
//    }
//
//
//
//


package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ChatScreen : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: messageAdapter
    private lateinit var messageList: ArrayList<messaage>
    private lateinit var mdbref: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userList : ChatUserAdapter

    var recciverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatscreen)
        mdbref = FirebaseDatabase.getInstance().getReference()
        messageList = ArrayList()
        messageAdapter = messageAdapter(this,messageList)
        chatRecyclerView = findViewById(R.id.chatt)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter
        messageBox = findViewById(R.id.messageBox)
        // Get the data from the intent
        val imageURL = intent.getStringExtra("imageUrl")
        val name = intent.getStringExtra("name")
        val recciverUid = intent.getStringExtra("uid") // Receive the receiver UID
        sendButton = findViewById(R.id.send3)
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mdbref = FirebaseDatabase.getInstance().getReference()

        val UserNameDisplayChatScreen = findViewById<TextView>(R.id.UserNameDisplayChatScreen)
        val displaydp = findViewById<ImageView>(R.id.displaydp)
        val prev = findViewById<ImageView>(R.id.prev)

        UserNameDisplayChatScreen.text = name
        Picasso.get().load(imageURL).into(displaydp)
        prev.setOnClickListener {
            val intent = Intent(this, Homescreen::class.java)
            startActivity(intent)
        }

        if (senderUid != null && recciverUid != null) {
            senderRoom = senderUid + recciverUid
            recciverRoom = recciverUid + senderUid

            // Rest of your code ...

//            mdbref.child("chats").child(recciverRoom!!).child("messages")
//                .addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        val newMessages = ArrayList<messaage>()
//                        for (postSnap in snapshot.children) {
//                            val boxo = postSnap.getValue(messaage::class.java)
//                            newMessages.add(boxo!!)
//                        }
//                        messageList.addAll(newMessages)
//                        messageAdapter.notifyDataSetChanged()
//                    }
//                    override fun onCancelled(error: DatabaseError) {
//
//                    }
//                })

            mdbref.child("chats").child(recciverRoom!!).child("messages")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        messageList.clear() // Clear the list before adding new messages
                        for (postSnap in snapshot.children) {
                            val boxo = postSnap.getValue(messaage::class.java)
                            boxo?.let { messageList.add(it) }
                        }
                        messageAdapter.notifyDataSetChanged()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })

            // Rest of your code ...

            sendButton.setOnClickListener {
                val message = messageBox.text.toString()
                if (!message.isNullOrEmpty()) {
                    val messageObject = messaage(message, senderUid)

                    // Push the message to sender's room
                    mdbref.child("chats").child(senderRoom!!).child("messages").push()
                        .setValue(messageObject)
                        .addOnSuccessListener {
                            // Handle success
                        }

                    // Push the same message to receiver's room
                    mdbref.child("chats").child(recciverRoom!!).child("messages").push()
                        .setValue(messageObject)
                        .addOnSuccessListener {
                            // Handle success
                        }

                    messageBox.setText("")
                } else {
                    // Handle empty message
                }
            }
        } else {
            // Handle the case where either senderUid or recciverUid is null

            if (senderUid == null && recciverUid == null) {
                Toast.makeText(this, "Sender and Receiver information are missing.", Toast.LENGTH_SHORT).show()
            } else if (senderUid == null) {
                Toast.makeText(this, "Sender information is missing.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Receiver information is missing.", Toast.LENGTH_SHORT).show()
            }

            // Optionally, you might want to finish the activity or take appropriate action.
        }
    }
}
