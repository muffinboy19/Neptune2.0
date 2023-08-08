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
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.security.MessageDigest


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
        val imageURL  = intent.getStringExtra("imageUrl")
        val name = intent.getStringExtra("name")
        val Recciveruid :String? = intent.getStringExtra("uid")
        val senderUId :String? = FirebaseAuth.getInstance().currentUser?.uid
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



        senderRoom = senderUId + Recciveruid
        recciverRoom = Recciveruid + senderUId

        Toast.makeText(this,"$senderRoom",Toast.LENGTH_SHORT).show()

        Toast.makeText(this,"$recciverRoom",Toast.LENGTH_SHORT).show()

        messageBox = findViewById(R.id.messageBox)
        chatRecyclerView = findViewById(R.id.chatt)
        sendButton = findViewById(R.id.send3)
        messageList =  ArrayList()
        messageAdapter = messageAdapter(this,messageList)

        chatRecyclerView.layoutManager= LinearLayoutManager(this)
        chatRecyclerView.adapter =messageAdapter
        mdbref.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for( postSnap in snapshot.children){
                        val boxo = postSnap.getValue(messaage::class.java)
                    messageList.add(boxo!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        // Inside your onCreate method in ChatScreen activity
// ... (Previous code)

// Set up the listener for the receiver's chat room
        mdbref.child("chats").child(recciverRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnap in snapshot.children) {
                        val boxo = postSnap.getValue(messaage::class.java)
                        messageList.add(boxo!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled
                }
            })

// ... (Rest of your code)





        sendButton.setOnClickListener {


            val message = messageBox.text.toString()
            val messageObject = messaage(message,senderUId)


            mdbref.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject)
                .addOnSuccessListener {

                }

            mdbref.child("chats").child(recciverRoom!!).child("messages").push()
                .setValue(messageObject)
                .addOnSuccessListener {

                }



            messageBox.setText("")
        }
    }
}




