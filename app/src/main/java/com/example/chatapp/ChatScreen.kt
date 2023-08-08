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
    var recciverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatscreen)


        mdbref = FirebaseDatabase.getInstance().getReference()
        messageList = ArrayList()
        messageAdapter = messageAdapter(this, messageList)
        chatRecyclerView = findViewById(R.id.chatt)
        sendButton = findViewById(R.id.send3)
        val UserNameDisplayChatScreen = findViewById<TextView>(R.id.UserNameDisplayChatScreen)
        val displaydp = findViewById<ImageView>(R.id.displaydp)
        val prev = findViewById<ImageView>(R.id.prev)



        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter
        messageBox = findViewById(R.id.messageBox)


        val imageURL = intent.getStringExtra("imageUrl")
        val name = intent.getStringExtra("name")
        val recciverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        UserNameDisplayChatScreen.text = name
        Picasso.get().load(imageURL).into(displaydp)




        prev.setOnClickListener {
            val intent = Intent(this, Homescreen::class.java)
            startActivity(intent)
        }


        // this is code is the code for understanidg the chat logic
        if (senderUid != null && recciverUid != null) {
            senderRoom = senderUid + recciverUid
            recciverRoom = recciverUid + senderUid

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
                        Toast.makeText(this@ChatScreen, "ERROR", Toast.LENGTH_SHORT).show()
                    }
                })


            sendButton.setOnClickListener {

                val message = messageBox.text.toString()
                if (!message.isNullOrEmpty()) {
                    val messageObject = messaage(message, senderUid)


                    mdbref.child("chats").child(senderRoom!!).child("messages").push()
                        .setValue(messageObject)
                        .addOnSuccessListener {
                        }

                    mdbref.child("chats").child(recciverRoom!!).child("messages").push()
                        .setValue(messageObject)
                        .addOnSuccessListener {
                        }

                    messageBox.setText("")
                } else {

                }
            }
        } else {
            if (senderUid == null && recciverUid == null) {
                Toast.makeText(
                    this,
                    "Sender and Receiver information are missing.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (senderUid == null) {
                Toast.makeText(this, "Sender information is missing.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Receiver information is missing.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
