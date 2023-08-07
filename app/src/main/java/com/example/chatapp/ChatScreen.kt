package com.example.chatapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class ChatScreen : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
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
        val UserNameDisplayChatScreen = findViewById<TextView>(R.id.UserNameDisplayChatScreen)
        val name = intent.getStringExtra("name")
        val Recciveruid = intent.getStringExtra("uid")




        val displaydp = findViewById<ImageView>(R.id.displaydp)



        mdbref = FirebaseDatabase.getInstance().getReference()
        val prev = findViewById<ImageView>(R.id.prev)
        prev.setOnClickListener {
            val intent = Intent(this, Homescreen::class.java)
            startActivity(intent)
        }


        val senderUId = FirebaseAuth.getInstance().currentUser?.uid
        if (Recciveruid != null && senderUId != null) {
            if (Recciveruid < senderUId) {
                senderRoom = Recciveruid + senderUId
            } else {
                senderRoom = senderUId + Recciveruid
            }
            recciverRoom = Recciveruid
        }

        UserNameDisplayChatScreen.text = name



        messageList = ArrayList()
        messageAdapter = messageAdapter(this, messageList)
        messageRecyclerView = findViewById(R.id.chatt)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.send3)


        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter



        mdbref.child("chats").child(senderRoom!!).child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnap in snapshot.children) {
                        val messaage = postSnap.getValue(messaage::class.java)
                        messageList.add(messaage!!)
                    }
                    messageAdapter.notifyDataSetChanged()



                    if (messageAdapter.itemCount > 0) {
                        messageRecyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
                        messageRecyclerView.postDelayed({
                            messageRecyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
                        }, 200)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event if needed
                    Toast.makeText(
                        this@ChatScreen,
                        "Database error: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            })
        sendButton.setOnClickListener {
//
            // here the messaeg is addeed to the data base
            val message: String = messageBox.text.toString().trim()

            val messageObject = messaage(message, senderUId)
            mdbref.child("chats").child(senderRoom!!).child("message").push()
                .setValue(messageObject).addOnSuccessListener {

                }
            mdbref.child("chats").child(recciverRoom!!).child("message").push()
                .setValue(messageObject).addOnSuccessListener {

                }

            messageAdapter.notifyDataSetChanged()
            messageBox.setText("")
            // Get the message text
//            val message: String = messageBox.text.toString()
//
//            // Check if the message is not empty
//            if (message.isNotEmpty()) {
//                val messageObject = messaage(message, senderUId)
//
//                // Use addOnCompleteListener to handle successful completion
//                mdbref.child("chats").child(senderRoom!!).child("message").push()
//                    .setValue(messageObject)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            // Message sent successfully
//                            Toast.makeText(this@ChatScreen, "Message sent successfully", Toast.LENGTH_SHORT).show()
//                        } else {
//                            // Failed to send message
//                            Toast.makeText(this@ChatScreen, "Failed to send message", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    .addOnFailureListener { e ->
//                        // Handle failure, if any
//                        Toast.makeText(this@ChatScreen, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//                    }
//
//                mdbref.child("chats").child(recciverRoom!!).child("message").push()
//                    .setValue(messageObject)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            // Message sent successfully
//                            Toast.makeText(this@ChatScreen, "Message sent successfully", Toast.LENGTH_SHORT).show()
//                        } else {
//                            // Failed to send message
//                            Toast.makeText(this@ChatScreen, "Failed to send message", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    .addOnFailureListener { e ->
//                        // Handle failure, if any
//                        Toast.makeText(this@ChatScreen, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//                    }
//
//                // Clear the message box after sending
//                messageBox.setText("")
//            } else {
//                Toast.makeText(this@ChatScreen, "Please enter a message", Toast.LENGTH_SHORT).show()
//            }
//        }

        }


    }
}